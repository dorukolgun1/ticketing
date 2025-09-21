package ticketing.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.mapper.OrderMapper;
import ticketing.repository.InventoryRepository;
import ticketing.repository.OrderRepository;
import ticketing.repository.entity.OrderEntity;
import ticketing.service.domain.Order;
import ticketing.service.domain.enums.OrderStatus;
import ticketing.service.domain.enums.TicketType;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final InventoryRepository inventoryRepo;
    private final OrderRepository orderRepo;
    private final OrderMapper mapper;
    private final MeterRegistry registry;

    private Counter ticketsSold() { return Counter.builder("tickets_sold_total").register(registry); }
    private Counter conflicts()   { return Counter.builder("purchase_conflict_total").register(registry); }

    @Transactional
    public Order purchaseOptimistic(String idempotencyKey, String eventCode, TicketType type, int qty) {
        var existing = orderRepo.findByOrderCode(idempotencyKey);
        if (existing.isPresent()) return mapper.toDomain(existing.get());

        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                var inv = inventoryRepo.findByEventCodeAndTicketType(eventCode, type)
                        .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
                if (inv.getSold() + qty > inv.getTotal()) throw new IllegalStateException("Not enough stock");

                var updated = inv.toBuilder().sold(inv.getSold() + qty).build();
                inventoryRepo.saveAndFlush(updated); // optimistic @Version

                var saved = orderRepo.save(newOrderEntity(idempotencyKey, eventCode, type, qty));
                ticketsSold().increment(qty);
                return mapper.toDomain(saved);
            } catch (ObjectOptimisticLockingFailureException ex) {
                conflicts().increment();
            }
        }
        throw new IllegalStateException("Concurrent update, please retry");
    }

    @Transactional
    public Order purchasePessimistic(String idempotencyKey, String eventCode, TicketType type, int qty) {
        var existing = orderRepo.findByOrderCode(idempotencyKey);
        if (existing.isPresent()) return mapper.toDomain(existing.get());

        var inv = inventoryRepo.lockForUpdate(eventCode, type)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
        if (inv.getSold() + qty > inv.getTotal()) throw new IllegalStateException("Not enough stock");

        inventoryRepo.saveAndFlush(inv.toBuilder().sold(inv.getSold() + qty).build());

        var saved = orderRepo.save(newOrderEntity(idempotencyKey, eventCode, type, qty));
        ticketsSold().increment(qty);
        return mapper.toDomain(saved);
    }

    private OrderEntity newOrderEntity(String orderCode, String eventCode, TicketType type, int qty) {
        if (orderCode == null || orderCode.isBlank()) orderCode = UUID.randomUUID().toString();
        return OrderEntity.builder()
                .orderCode(orderCode).eventCode(eventCode).ticketType(type)
                .quantity(qty).status(OrderStatus.CONFIRMED).createdAt(Instant.now())
                .build();
    }
}
