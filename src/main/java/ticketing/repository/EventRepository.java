package ticketing.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.entity.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    Optional<EventEntity> findByCode(String code);
    boolean existsByCode(String code);
}
