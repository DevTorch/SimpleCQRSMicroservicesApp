package cqrs_example.queryservice.repository;

import cqrs_example.queryservice.model.entity.ProcessedMessagesEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KafkaMessageStorageRepository extends JpaRepository<ProcessedMessagesEvent, String> {

    Optional<ProcessedMessagesEvent> findByMessageId(String messageId);
}
