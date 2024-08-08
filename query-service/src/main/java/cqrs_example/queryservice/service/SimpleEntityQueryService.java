package cqrs_example.queryservice.service;

import cqrs_example.kafkacore.events.SimpleEntitySynchronisationEvent;

import java.util.List;

public interface SimpleEntityQueryService {
    void saveAllProcessedEntityEvents(List<SimpleEntitySynchronisationEvent> events);
}
