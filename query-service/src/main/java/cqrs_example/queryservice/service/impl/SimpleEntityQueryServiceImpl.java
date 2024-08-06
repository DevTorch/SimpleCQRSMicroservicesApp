package cqrs_example.queryservice.service.impl;

import cqrs_example.kafkacore.events.SimpleEntitySynchronisationEvent;
import cqrs_example.queryservice.model.mapper.SimpleEventMapper;
import cqrs_example.queryservice.repository.SimpleEntityQueryRepository;
import cqrs_example.queryservice.service.SimpleEntityQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleEntityQueryServiceImpl implements SimpleEntityQueryService {

    private final SimpleEntityQueryRepository repository;
    private final SimpleEventMapper mapper;

    @Override
    @Transactional
    public void saveAllProcessedEntityEvents(List<SimpleEntitySynchronisationEvent> events) {
        repository.saveAll(mapper.eventsToEntities(events));

    }
}
