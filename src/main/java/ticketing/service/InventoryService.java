package ticketing.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.dto.InventoryDto;
import ticketing.mapper.InventoryMapper;
import ticketing.repository.InventoryRepository;
import ticketing.entity.InventoryEntity;
import ticketing.enums.TicketType;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryService(InventoryRepository inventoryRepository,
                            InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }


    @Transactional(readOnly = true)
    public List<InventoryDto> getByEvent(String eventCode) {
        return inventoryRepository.findByEventCode(eventCode).stream()
                .map(inventoryMapper::toDomain)
                .map(inventoryMapper::toDto)
                .toList();
    }


    @Transactional
    public void reserve(String eventCode, TicketType ticketType, int quantity, String strategy) {
        if ("pessimistic".equalsIgnoreCase(strategy)) {
            reservePessimistic(eventCode, ticketType, quantity);
        } else {
            reserveOptimistic(eventCode, ticketType, quantity);
        }
    }

    /* ----- Private helpers ----- */

    private void ensureAvailable(InventoryEntity inv, int quantity) {
        int remaining = inv.getTotal() - inv.getSold();
        if (quantity <= 0 || remaining < quantity) {
            throw new IllegalStateException("Insufficient inventory for " + inv.getEventCode() +
                    " / " + inv.getTicketType() + " (remaining=" + remaining + ", requested=" + quantity + ")");
        }
    }


    @Transactional
    protected void reserveOptimistic(String eventCode, TicketType ticketType, int quantity) {
        InventoryEntity inv = inventoryRepository
                .findByEventCodeAndTicketType(eventCode, ticketType)
                .orElseThrow(() -> new IllegalStateException("Inventory row not found for event=" +
                        eventCode + ", type=" + ticketType));
        ensureAvailable(inv, quantity);
        inv.setSold(inv.getSold() + quantity);

    }


    @Transactional
    protected void reservePessimistic(String eventCode, TicketType ticketType, int quantity) {
        InventoryEntity inv = inventoryRepository
                .findForUpdate(eventCode, ticketType)
                .orElseThrow(() -> new IllegalStateException("Inventory row not found for event=" +
                        eventCode + ", type=" + ticketType));
        ensureAvailable(inv, quantity);
        inv.setSold(inv.getSold() + quantity);
    }
}
