package cqrs_example.queryservice.service.events;

import cqrs_example.queryservice.model.dto.ProcessedMessageDto;
import cqrs_example.queryservice.model.entity.ProcessedMessagesEvent;
import cqrs_example.queryservice.model.mapper.ProcessedMessagesMapper;
import cqrs_example.queryservice.repository.KafkaMessageStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaMessageStorageServiceImpl implements KafkaMessageStorageService {

    @Override
    @Transactional(readOnly = true)
    public Page<ProcessedMessageDto> getPageableProcessedMessages(Pageable pageable) {
        return messageStorageRepository.findAll(pageable).map(messagesMapper::toDto);
    }

    private final KafkaMessageStorageRepository messageStorageRepository;
    private final ProcessedMessagesMapper messagesMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcessedMessageDto> findByMessageId(String messageId) {
        var messagesEvent = messageStorageRepository.findByMessageId(messageId);
        return messagesEvent.map(messagesMapper::toDto);
    }

    @Override
    @Transactional
    public void storeMessage(ProcessedMessagesEvent event) {
        messageStorageRepository.save(event);
    }

    @Override
    @Transactional
    public void deleteAllProcessed() {
        messageStorageRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcessedMessageDto> getProcessed() {
        return messagesMapper.toDtos(messageStorageRepository.findAll());
    }


}
