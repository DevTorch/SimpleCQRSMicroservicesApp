package cqrs_example.queryservice.config;

import cqrs_example.queryservice.handler.EventsScheduledProcessor;
import cqrs_example.queryservice.service.SimpleEntityQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncExecutorRunner {

    public AsyncExecutorRunner(SimpleEntityQueryService queryService) {

        EventsScheduledProcessor processor = new EventsScheduledProcessor();
        processor.setEntityService(queryService);
        processor.start();
    }
}
