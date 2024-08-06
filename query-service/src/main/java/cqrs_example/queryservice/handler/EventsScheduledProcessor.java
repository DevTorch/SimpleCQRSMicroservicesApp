package cqrs_example.queryservice.handler;

import cqrs_example.kafkacore.events.SimpleEntitySynchronisationEvent;
import cqrs_example.queryservice.service.SimpleEntityQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class EventsScheduledProcessor {

    @Autowired
    private SimpleEntityQueryService queryService;

    private static int count = 0;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    /**
     * Запускаем ScheduledTask по расписанию для обновления базы данных.
     * Задача запускается каждые 5 минут и вызывает метод `updateQueryServiceDatabase`.
     */
    public void start() {
        log.info("Start EventsScheduledProcessor {}", LocalDateTime.now().format(dateTimeFormatter));

        Runnable scheduledTask = () -> {
            updateQueryServiceDatabase();
        };

        scheduler.scheduleAtFixedRate(scheduledTask, 0, 1, TimeUnit.MINUTES);
    }

    /**
     * Синхронизируем базу данных QueryService с базой данных CommonService, собирая события Kafka
     * в буфере EventsBuffer, после чего вызываем метод 'getAndClearBuffer' и очищаем буфер,
     * забирая ивенты и обновляя базу данных.
     */
    private void updateQueryServiceDatabase() {
        List<SimpleEntitySynchronisationEvent> events = EventsBuffer.getAndClearBuffer();
        count++;
        log.info("Count: {}, ListSize: {}, dateTime: {}", count, events.size(), LocalDateTime.now().format(dateTimeFormatter));
        if (!events.isEmpty()) {
            queryService.saveAllProcessedEntityEvents(events);
            log.info("Update QueryService database");
        } else {
            log.info("No events in buffer, Count: {}", count);
        }
    }
}