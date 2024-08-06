package cqrs_example.queryservice.service.events;

import cqrs_example.queryservice.model.entity.ProcessedMessagesEvent;
import cqrs_example.queryservice.repository.KafkaMessageStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaMessageStorageServiceImpl implements KafkaMessageStorageService {

    private final KafkaMessageStorageRepository messageStorageRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcessedMessagesEvent> findByMessageId(String messageId) {
        return messageStorageRepository.findByMessageId(messageId);
    }

    @Override
    @Transactional
    public void storeMessage(ProcessedMessagesEvent event) {
        messageStorageRepository.save(event);
    }
}
