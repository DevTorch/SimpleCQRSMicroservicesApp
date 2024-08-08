package cqrs_example.queryservice.handler;

import cqrs_example.kafkacore.events.SimpleEntitySynchronisationEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EventsBuffer {

    private static final List<SimpleEntitySynchronisationEvent> buffer = new ArrayList<>();

    public synchronized static void addToBuffer(SimpleEntitySynchronisationEvent event) {
      buffer.add(event);
    }

    public synchronized static List<SimpleEntitySynchronisationEvent> getAndClearBuffer() {
      List<SimpleEntitySynchronisationEvent> events = new ArrayList<>(buffer);
      buffer.clear();
      return events;
    }
}
