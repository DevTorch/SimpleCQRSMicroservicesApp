package cqrs_example.queryservice.model.mapper;

import cqrs_example.queryservice.model.dto.SimpleEntityTransferModelDTO;
import cqrs_example.queryservice.model.entity.SimpleEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SimpleMapper {

    //Если поля entity и dto имеют одинаковые имена, то можно не использовать @Mapping

    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "email", source = "email")
    SimpleEntityTransferModelDTO toDTO(SimpleEntity entity);

    @InheritInverseConfiguration
    @Mapping(target = "description", ignore = true)
    SimpleEntity toEntity(SimpleEntityTransferModelDTO dto);

    List<SimpleEntityTransferModelDTO> toDTOs(List<SimpleEntity> entities);

    @InheritInverseConfiguration
    @Mapping(target = "description", ignore = true)
    List<SimpleEntity> toEntities(List<SimpleEntityTransferModelDTO> dtos);

    @InheritConfiguration(name = "toEntity")
    SimpleEntity updateWithNull(SimpleEntityTransferModelDTO simpleEntityTransferModelDTO, @MappingTarget SimpleEntity simpleEntity);
}
