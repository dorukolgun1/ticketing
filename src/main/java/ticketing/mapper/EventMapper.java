package ticketing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ticketing.controller.dto.EventDto;
import ticketing.repository.entity.EventEntity;
import ticketing.service.domain.Event;

@Mapper(componentModel = "spring", config = MapStructConfig.class)
public interface EventMapper {

    // Entity -> Domain
    @Mapping(target = "id",        source = "id")
    @Mapping(target = "code",      source = "code")
    @Mapping(target = "name",      source = "name")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime",   source = "endTime")
    Event toDomain(EventEntity e);

    // Domain -> Entity
    EventEntity toEntity(Event d);

    // Domain -> DTO
    EventDto toDto(Event d);
}
