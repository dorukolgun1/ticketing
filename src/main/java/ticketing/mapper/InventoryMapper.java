package ticketing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ticketing.dto.InventoryDto;
import ticketing.entity.InventoryEntity;
import ticketing.domain.Inventory;

@Mapper(componentModel = "spring", config = MapStructConfig.class)
public interface InventoryMapper {

    // Entity -> Domain
    @Mappings({
            // Entity'de version yoksa uyarı çıkmasın
            @Mapping(target = "version", ignore = true)
    })
    Inventory toDomain(InventoryEntity e);

    // Domain -> Entity
    @Mappings({
            // id JPA tarafından yönetiliyor
            @Mapping(target = "id", ignore = true),
            // version JPA tarafından yönetiliyor
            @Mapping(target = "version", ignore = true)
    })
    InventoryEntity toEntity(Inventory d);

    // Domain -> DTO (available = total - sold)
    @Mapping(target = "available", expression = "java(d.getTotal() - d.getSold())")
    InventoryDto toDto(Inventory d);
}
