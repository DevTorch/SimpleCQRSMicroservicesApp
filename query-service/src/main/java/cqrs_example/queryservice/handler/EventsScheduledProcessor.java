package cqrs_example.queryservice.handler;

import cqrs_example.kafkacore.events.SimpleEntitySynchronisationEvent;
import cqrs_example.queryservice.service.SimpleEntityQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class EventsScheduledProcessor {

    @Autowired
    private SimpleEntityQueryService queryService;

    /**
     * Запускаем ScheduledTask по расписанию для обновления базы данных.
     * Задача запускается каждые 5 минут и вызывает метод `updateQueryServiceDatabase`.
     */
    public void start() {
        Runnable scheduledTask = this::updateQueryServiceDatabase;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(scheduledTask, 5, 5, TimeUnit.MINUTES);
    }

    /**
     * Синхронизируем базу данных QueryService с базой данных CommonService, собирая события Kafka
     * в буфере EventsBuffer, после чего вызываем метод 'getAndClearBuffer' и очищаем буфер,
     * забирая ивенты и обновляя базу данных.
     */
    private void updateQueryServiceDatabase() {
        List<SimpleEntitySynchronisationEvent> events = EventsBuffer.getAndClearBuffer();
        if (!events.isEmpty()) {
            queryService.saveAllProcessedEntityEvents(events);
        }
    }
}