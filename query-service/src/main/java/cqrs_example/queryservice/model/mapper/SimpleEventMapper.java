package cqrs_example.queryservice.model.mapper;


import cqrs_example.kafkacore.events.SimpleEntitySynchronisationEvent;
import cqrs_example.queryservice.model.entity.SimpleEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SimpleEventMapper {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "fullName", source = "entity.fullName")
    @Mapping(target = "email", source = "entity.email")
    @Mapping(target = "description", source = "entity.description", defaultValue = "")
    SimpleEntitySynchronisationEvent entityToEvent(SimpleEntity entity);

    @InheritInverseConfiguration
    SimpleEntity eventToEntity(SimpleEntitySynchronisationEvent event);
}
