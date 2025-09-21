package ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.repository.entity.EventEntity;

import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    Optional<EventEntity> findByCode(String code);
}
