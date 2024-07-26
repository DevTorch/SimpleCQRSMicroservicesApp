package cqrs_example.commandservice.model.mapper;

import cqrs_example.commandservice.model.dto.SimpleEntityResponseDTO;
import cqrs_example.commandservice.model.entity.SimpleEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SimpleMapper {

    //Если поля entity и dto имеют одинаковые имена, то можно не использовать @Mapping

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "fullName", source = "entity.fullName")
    @Mapping(target = "email", source = "entity.email")
    SimpleEntityResponseDTO toDTO(SimpleEntity entity);

    @InheritInverseConfiguration
    @Mapping(target = "description", ignore = true)
    SimpleEntity toEntity(SimpleEntityResponseDTO dto);

    List<SimpleEntityResponseDTO> toDTOs(List<SimpleEntity> entities);

    @InheritInverseConfiguration
    @Mapping(target = "description", ignore = true)
    List<SimpleEntity> toEntities(List<SimpleEntityResponseDTO> dtos);

}
