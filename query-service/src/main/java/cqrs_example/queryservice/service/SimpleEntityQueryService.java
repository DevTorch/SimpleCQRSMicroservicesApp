package cqrs_example.queryservice.service;

import cqrs_example.kafkacore.events.SimpleEntitySynchronisationEvent;

public interface SimpleEntityQueryService {
    void saveAllProcessedEntityEvents(Iterable<SimpleEntitySynchronisationEvent> events);
}
