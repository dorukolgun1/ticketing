package ticketing.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.persistence.OptimisticLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.dto.OrderDto;
import ticketing.dto.PurchaseRequest;
import ticketing.mapper.OrderMapper;
import ticketing.repository.OrderRepository;
import ticketing.entity.OrderEntity;
import ticketing.enums.OrderStatus;

import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final InventoryService inventoryService;
    private final OrderMapper mapper;

    private final Counter soldCounter;
    private final Counter conflictCounter;
    private final Timer purchaseTimer;

    public OrderService(OrderRepository orderRepo,
                        InventoryService inventoryService,
                        OrderMapper mapper,
                        MeterRegistry registry) {
        this.orderRepo = orderRepo;
        this.inventoryService = inventoryService;
        this.mapper = mapper;
        this.soldCounter = Counter.builder("tickets_sold_total").register(registry);
        this.conflictCounter = Counter.builder("purchase_conflict_total").register(registry);
        this.purchaseTimer = Timer.builder("purchase_latency")
                .publishPercentiles(0.95, 0.99)
                .register(registry);
    }

    public Page<OrderDto> list(String eventCode, Pageable pageable) {
        return orderRepo.findByEventCode(eventCode, pageable)
                .map(mapper::toDomain)
                .map(mapper::toDto);
    }

    public OrderDto get(long id) {
        return orderRepo.findById(id)
                .map(mapper::toDomain)
                .map(mapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + id));
    }

    public OrderDto purchase(PurchaseRequest req, String idempotencyKey, String strategy) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalStateException("Idempotency-Key header is required");
        }
        var existing = orderRepo.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            return mapper.toDto(mapper.toDomain(existing.get()));
        }
        return purchaseTimer.record(() -> doPurchaseWithRetry(req, idempotencyKey, strategy));
    }

    private OrderDto doPurchaseWithRetry(PurchaseRequest req, String idempotencyKey, String strategy) {
        int attempts = 0;
        int maxAttempts = 3;
        while (true) {
            try {
                return doPurchaseOnce(req, idempotencyKey, strategy);
            } catch (OptimisticLockException ex) {
                conflictCounter.increment();
                if (++attempts >= maxAttempts) {
                    throw new IllegalStateException("Concurrency conflict, please retry");
                }
                // küçük backoff istersen: Thread.sleep(10L);
            }
        }
    }

    @Transactional
    protected OrderDto doPurchaseOnce(PurchaseRequest req, String idempotencyKey, String strategy) {
        // Stok rezervasyonu/güncellemesi (enum ile)
        inventoryService.reserve(req.eventCode(), req.ticketType(), req.quantity(), strategy);

        // Order kaydet (ENUM alanlar)
        OrderEntity ent = OrderEntity.builder()
                .eventCode(req.eventCode())
                .ticketType(req.ticketType())
                .quantity(req.quantity())
                .status(OrderStatus.CONFIRMED)
                .idempotencyKey(idempotencyKey)
                .build();

        var saved = orderRepo.save(ent);

        soldCounter.increment(req.quantity());
        return mapper.toDto(mapper.toDomain(saved));
    }
}
