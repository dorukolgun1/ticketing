package ticketing.mapper;

import org.mapstruct.Mapper;
import ticketing.repository.entity.OrderEntity;
import ticketing.service.domain.Order;

@Mapper(componentModel = "spring", config = MapStructConfig.class)
public interface OrderMapper {
    Order toDomain(OrderEntity e);
    OrderEntity toEntity(Order d);
}
