package cqrs_example.queryservice.service.events;

import cqrs_example.kafkacore.events.SimpleEntitySynchronisationEvent;
import cqrs_example.queryservice.model.entity.ProcessedMessagesEvent;

import java.util.Optional;

public interface KafkaMessageStorageService {

    Optional<ProcessedMessagesEvent> findByMessageId(String messageId);

    void storeMessage(ProcessedMessagesEvent event);
}
