package ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.repository.entity.OrderEntity;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderCode(String orderCode);
}
