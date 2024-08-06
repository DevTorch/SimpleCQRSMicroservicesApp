package cqrs_example.queryservice;

import cqrs_example.queryservice.handler.EventsScheduledProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QueryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueryServiceApplication.class, args);

		EventsScheduledProcessor processor = new EventsScheduledProcessor();
		processor.start();

	}

}
