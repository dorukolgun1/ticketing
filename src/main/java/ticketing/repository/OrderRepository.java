package ticketing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.entity.OrderEntity;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByIdempotencyKey(String idempotencyKey);
    Page<OrderEntity> findByEventCode(String eventCode, Pageable pageable);
}
