package cqrs_example.commandservice.service.impl;

import cqrs_example.commandservice.exception.KafkaSenderException;
import cqrs_example.commandservice.exception.SimpleEntityNotFoundException;
import cqrs_example.commandservice.model.dto.SimpleEntityResponseDTO;
import cqrs_example.commandservice.model.mapper.SimpleEventMapper;
import cqrs_example.commandservice.model.mapper.SimpleMapper;
import cqrs_example.commandservice.repository.SimpleEntityRepository;
import cqrs_example.commandservice.service.SimpleEntityService;
import cqrs_example.kafkacore.constants.CqrsCoreConstants;
import cqrs_example.kafkacore.events.EventTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleEntityServiceImplementation implements SimpleEntityService {

    private final SimpleEntityRepository repository;
    private final SimpleMapper simpleMapper;
    private final SimpleEventMapper eventMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public void save(SimpleEntityResponseDTO entityRequestDTO) {
        var savedEntity = repository.save(simpleMapper.toEntity(entityRequestDTO));
        var event = eventMapper.entityToEvent(savedEntity);

        ProducerRecord<String, Object> queryRecord = new ProducerRecord<>(
                CqrsCoreConstants.CQRS_EVENTS_TOPIC,
                String.valueOf(event.id()),
                event);

        queryRecord.headers()
                .add("messageId", UUID.randomUUID().toString().getBytes())
                .add("messageKey", event.id().toString().getBytes())
                .add("eventType", EventTypeEnum.CREATE.toString().getBytes());

        CompletableFuture<SendResult<String, Object>> processingEvent = kafkaTemplate.send(queryRecord);

        processingEvent.whenComplete(
                (result, ex) -> {
                    if (ex == null) {
                        log.info("Event was sent successfully {}", result.getRecordMetadata());
                        log.info("Topic: {}", result.getRecordMetadata().topic());
                        log.info("Partition: {}", result.getRecordMetadata().partition());
                        log.info("Offset: {}", result.getRecordMetadata().offset());
                    } else {
                        log.error("Filed to send event {}", ex.getMessage());
                        throw new KafkaSenderException(LocalDateTime.now(), ex.getMessage());
                    }
                }
        );

    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void update(Long id, SimpleEntityResponseDTO entityRequestDTO) {
        var entity = repository.findById(id).orElseThrow(
                () -> new SimpleEntityNotFoundException(String.format("Filed to update entity with id: %d", id)));
        entity.setEmail(entityRequestDTO.email());
        entity.setFullName(entityRequestDTO.fullName());
        entity.setDescription(String.format("Last Update: %s", Instant.now().toString()));
        repository.save(entity);
    }

}
