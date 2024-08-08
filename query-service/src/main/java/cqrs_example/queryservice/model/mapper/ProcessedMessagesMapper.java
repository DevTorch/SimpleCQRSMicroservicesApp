package cqrs_example.queryservice.model.mapper;

import cqrs_example.queryservice.model.dto.ProcessedMessageDto;
import cqrs_example.queryservice.model.entity.ProcessedMessagesEvent;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProcessedMessagesMapper {
    ProcessedMessagesEvent toEntity(ProcessedMessageDto processedMessageDto);

    ProcessedMessageDto toDto(ProcessedMessagesEvent processedMessagesEvent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProcessedMessagesEvent partialUpdate(ProcessedMessageDto processedMessageDto, @MappingTarget ProcessedMessagesEvent processedMessagesEvent);

    List<ProcessedMessageDto> toDtos(List<ProcessedMessagesEvent> all);

    List<ProcessedMessagesEvent> toEntities(List<ProcessedMessageDto> dtos);
}