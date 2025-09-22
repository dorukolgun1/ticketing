package ticketing.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ticketing.entity.InventoryEntity;
import ticketing.enums.TicketType;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    // InventoryController -> InventoryService#getByEvent için
    List<InventoryEntity> findByEventCode(String eventCode);

    // Satın alma akışında stok satırını bulmak için
    Optional<InventoryEntity> findByEventCodeAndTicketType(String eventCode, TicketType ticketType);

    // Pessimistic locking gerektiğinde aynı satırı write-lock ile çekmek için
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InventoryEntity i " +
            "where i.eventCode = :eventCode and i.ticketType = :ticketType")
    Optional<InventoryEntity> findForUpdate(@Param("eventCode") String eventCode,
                                            @Param("ticketType") TicketType ticketType);
}
