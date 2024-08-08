package cqrs_example.queryservice.handler;

import cqrs_example.kafkacore.events.SimpleEntitySynchronisationEvent;
import cqrs_example.queryservice.service.SimpleEntityQueryService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EventsScheduledProcessor {

    private SimpleEntityQueryService queryService;
    private static int count = 0;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    /**
     * Starts the EventsScheduledProcessor, which schedules a task to run every 5 minutes.
     * The task retrieves events from the EventsBuffer, saves them to the QueryService database,
     * and logs the count of events and the current time. If the buffer is empty, it only logs
     * the count.
     */
    public void start() {
        log.info("Start EventsScheduledProcessor {}", LocalDateTime.now().format(dateTimeFormatter));
        scheduler.scheduleAtFixedRate(() -> {
            List<SimpleEntitySynchronisationEvent> events = EventsBuffer.getAndClearBuffer();
            if (events.isEmpty()) {
                count++;
                log.info("Just Count in empty buffer: {}, dateTime: {}", count, LocalDateTime.now().format(dateTimeFormatter));
                return;
            }
            queryService.saveAllProcessedEntityEvents(events);
            log.info("Thread Circle Count: {}, ListSize: {}, dateTime: {}", ++count, events.size(), LocalDateTime.now().format(dateTimeFormatter));
            log.info("Update QueryService database");
        }, 1, 5, TimeUnit.MINUTES);
    }
    public void setEntityService(SimpleEntityQueryService queryService) {
        this.queryService = queryService;
    }
}



