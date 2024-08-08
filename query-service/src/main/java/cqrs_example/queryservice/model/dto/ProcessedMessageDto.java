package cqrs_example.queryservice.model.dto;

import cqrs_example.kafkacore.events.EventTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link cqrs_example.queryservice.model.entity.ProcessedMessagesEvent}
 */
public record ProcessedMessageDto(
        Long id, String messageId,
        Long entityId,
        EventTypeEnum eventType,
        String eventBody,
        LocalDateTime receivedTimeStamp) implements Serializable {
}