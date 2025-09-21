package ticketing.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ticketing.repository.entity.InventoryEntity;
import ticketing.service.domain.enums.TicketType;
import jakarta.persistence.LockModeType;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByEventCodeAndTicketType(String eventCode, TicketType type);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InventoryEntity i where i.eventCode = :eventCode and i.ticketType = :type")
    Optional<InventoryEntity> lockForUpdate(@Param("eventCode") String eventCode, @Param("type") TicketType type);
}
