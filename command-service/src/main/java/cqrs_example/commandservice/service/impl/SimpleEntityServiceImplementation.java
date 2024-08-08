package cqrs_example.commandservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cqrs_example.commandservice.exception.KafkaSenderException;
import cqrs_example.commandservice.exception.SimpleEntityNotFoundException;
import cqrs_example.commandservice.model.dto.SimpleEntityTransferModelDTO;
import cqrs_example.commandservice.model.entity.SimpleEntity;
import cqrs_example.commandservice.model.mapper.SimpleEventMapper;
import cqrs_example.commandservice.model.mapper.SimpleMapper;
import cqrs_example.commandservice.repository.SimpleEntityCommandRepository;
import cqrs_example.commandservice.service.SimpleEntityService;
import cqrs_example.kafkacore.constants.CqrsCoreConstants;
import cqrs_example.kafkacore.events.EventTypeEnum;
import cqrs_example.kafkacore.events.SimpleEntitySynchronisationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleEntityServiceImplementation implements SimpleEntityService {

    private final ObjectMapper objectMapper;

    private final SimpleEntityCommandRepository repository;
    private final SimpleMapper simpleMapper;
    private final SimpleEventMapper eventMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public List<SimpleEntityTransferModelDTO> createMany(List<SimpleEntityTransferModelDTO> dtos) {

        //Create Producer Records for List items and send it to KafkaTemplate
        List<SimpleEntity> entities = repository.saveAll(simpleMapper.toEntities(dtos));

        log.info("Created {} entities. Last entity id: {}", entities.size(), entities.get(entities.size() - 1).getId());

        List<SimpleEntitySynchronisationEvent> events = entities.stream().map(eventMapper::entityToEvent).toList();

        List<ProducerRecord<String, Object>> producerRecords = events.stream().map(this::createProducerRecord).toList();
        CompletableFuture.allOf(producerRecords.stream().map(kafkaTemplate::send).toArray(CompletableFuture[]::new)).join();

        return simpleMapper.toDTOs(entities);
    }

    private ProducerRecord<String, Object> createProducerRecord(SimpleEntitySynchronisationEvent event) {
        ProducerRecord<String, Object> batchRecord = new ProducerRecord<>(
                CqrsCoreConstants.CQRS_EVENTS_TOPIC,
                String.valueOf(event.id()),
                event);

        batchRecord.headers()
                .add("messageId", UUID.randomUUID().toString().getBytes())
                .add("messageKey", event.id().toString().getBytes())
                .add("eventType", EventTypeEnum.CREATE.toString().getBytes());
        log.info("Created ProducerRecords");
        return batchRecord;
    }

    @Override
    @Transactional
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SimpleEntityTransferModelDTO create(SimpleEntityTransferModelDTO dto) {
        SimpleEntity savedEntity = repository.save(simpleMapper.toEntity(dto));
        SimpleEntitySynchronisationEvent event = eventMapper.entityToEvent(savedEntity);

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
        return simpleMapper.toDTO(savedEntity);
    }

    @Override
    @Transactional
    public SimpleEntityTransferModelDTO delete(Long id) {
        Optional<SimpleEntity> byId = repository.findById(id);
        if (byId.isPresent()) {
            repository.deleteById(id);
            return simpleMapper.toDTO(byId.get());
        }
        log.error("Entity with id {} not found", id);
        throw new SimpleEntityNotFoundException();
    }

    @Override
    @Transactional
    public SimpleEntityTransferModelDTO patch(Long id, JsonNode patchNode) throws IOException {
        SimpleEntity simpleEntity = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        SimpleEntityTransferModelDTO simpleEntityTransferModelDTO = simpleMapper.toDTO(simpleEntity);
        objectMapper.readerForUpdating(simpleEntityTransferModelDTO).readValue(patchNode);
        simpleMapper.updateWithNull(simpleEntityTransferModelDTO, simpleEntity);

        SimpleEntity resultSimpleEntity = repository.save(simpleEntity);
        return simpleMapper.toDTO(resultSimpleEntity);
    }

    @Override
    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException {
        Collection<SimpleEntity> simpleEntities = repository.findAllById(ids);

        for (SimpleEntity simpleEntity : simpleEntities) {
            SimpleEntityTransferModelDTO simpleEntityTransferModelDTO = simpleMapper.toDTO(simpleEntity);
            objectMapper.readerForUpdating(simpleEntityTransferModelDTO).readValue(patchNode);
            simpleMapper.updateWithNull(simpleEntityTransferModelDTO, simpleEntity);
        }

        List<SimpleEntity> resultSimpleEntities = repository.saveAll(simpleEntities);
        return resultSimpleEntities.stream()
                .map(SimpleEntity::getId)
                .toList();
    }

    @Override
    @Transactional
    public void deleteMany(List<Long> ids) {
        repository.deleteAllById(ids);
    }
}
