package cqrs_example.queryservice.service.events;

import cqrs_example.queryservice.model.dto.ProcessedMessageDto;
import cqrs_example.queryservice.model.entity.ProcessedMessagesEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface KafkaMessageStorageService {

    Optional<ProcessedMessageDto> findByMessageId(String messageId);

    void storeMessage(ProcessedMessagesEvent event);

    void deleteAllProcessed();

    List<ProcessedMessageDto> getProcessed();

    Page<ProcessedMessageDto> getPageableProcessedMessages(Pageable pageable);
}
