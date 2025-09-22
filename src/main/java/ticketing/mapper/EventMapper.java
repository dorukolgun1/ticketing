package ticketing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ticketing.dto.CreateEventRequest;
import ticketing.dto.EventDto;
import ticketing.entity.EventEntity;
import ticketing.domain.Event;

@Mapper(componentModel = "spring", config = MapStructConfig.class)
public interface EventMapper {

    // Request -> Domain
    @Mapping(target = "id", ignore = true)
    Event toDomain(CreateEventRequest req);

    // Entity <-> Domain
    Event toDomain(EventEntity e);


    EventEntity toEntity(Event d);

    // Domain -> DTO
    EventDto toDto(Event d);
}
