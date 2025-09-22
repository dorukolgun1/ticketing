package ticketing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ticketing.dto.OrderDto;
import ticketing.entity.OrderEntity;
import ticketing.domain.Order;

@Mapper(componentModel = "spring", config = MapStructConfig.class)
public interface OrderMapper {

    // Entity -> Domain (alan adları bire bir eşleşiyor)
    Order toDomain(OrderEntity e);

    // Domain -> Entity
    // id ve createdAt JPA tarafından yönetiliyor, idempotencyKey'i service set ediyor
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "idempotencyKey", ignore = true)
    OrderEntity toEntity(Order d);

    // Domain -> DTO
    OrderDto toDto(Order d);

    // (opsiyonel) DTO -> Domain
    Order toDomain(OrderDto dto);
}
