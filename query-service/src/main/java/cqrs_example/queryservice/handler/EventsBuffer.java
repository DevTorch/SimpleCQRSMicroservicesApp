package cqrs_example.queryservice.handler;

import cqrs_example.kafkacore.events.SimpleEntitySynchronisationEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class EventsBuffer {

    private static final AtomicReference<List<SimpleEntitySynchronisationEvent>> buffer = new AtomicReference<>(new ArrayList<>());

    public synchronized static void addToBuffer(SimpleEntitySynchronisationEvent event) {

      log.info("Event added to buffer: {}", event);
      buffer.get().add(event);
    }

    public synchronized static List<SimpleEntitySynchronisationEvent> getAndClearBuffer() {
      AtomicReference<List<SimpleEntitySynchronisationEvent>> events = new AtomicReference<>(new ArrayList<>(buffer.get()));
      buffer.get().clear();
      return events.get();
    }
}
