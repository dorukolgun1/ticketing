package ticketing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.repository.InventoryRepository;
import ticketing.repository.entity.InventoryEntity;
import ticketing.service.domain.enums.TicketType;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepo;

    @Transactional
    public void createIfNotExists(String eventCode, TicketType type, int total) {
        inventoryRepo.findByEventCodeAndTicketType(eventCode, type).orElseGet(() ->
                inventoryRepo.save(InventoryEntity.builder()
                        .eventCode(eventCode).ticketType(type).total(total).sold(0).build()));
    }
}
