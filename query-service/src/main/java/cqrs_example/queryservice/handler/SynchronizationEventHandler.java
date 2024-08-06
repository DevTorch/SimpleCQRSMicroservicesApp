package cqrs_example.queryservice.handler;

import cqrs_example.kafkacore.constants.CqrsCoreConstants;
import cqrs_example.kafkacore.events.EventTypeEnum;
import cqrs_example.kafkacore.events.SimpleEntitySynchronisationEvent;
import cqrs_example.queryservice.model.entity.ProcessedMessagesEvent;
import cqrs_example.queryservice.service.events.KafkaMessageStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = {CqrsCoreConstants.CQRS_EVENTS_TOPIC}, groupId = CqrsCoreConstants.GROUP_ID)
public class SynchronizationEventHandler {

    private final KafkaMessageStorageService storageService;

    @KafkaHandler
    public void handleSimpleEntitySynchronisationEven(
            @Payload SimpleEntitySynchronisationEvent event,
            @Header(name = "messageId") String messageId,
            @Header(name = KafkaHeaders.RECEIVED_KEY) String messageKey,
            @Header(name = "eventType") String eventType) {

        //Проверяем сообщение по messageId на существование. Если нет, то сохраняем в БД

        var storedMessagesEvent = storageService.findByMessageId(messageId);

        if (storedMessagesEvent.isPresent()) {
            log.info("Message event already processed: {}, messageId: {}, messageKey: {}, eventType: {}, event: {}",
                    event, messageId, messageKey, eventType, event);
            return;
        }

        storageService.storeMessage(new ProcessedMessagesEvent(
                messageId,
                Long.valueOf(messageKey),
                EventTypeEnum.valueOf(eventType),
                event.toString(),
                LocalDateTime.now()));

        log.info("Received event: {}, messageId: {}, messageKey: {}, eventType: {}, event: {}", event, messageId, messageKey, eventType, event);

        //TODO Логика сохранения нового события в БД
        /* Формируем буферный класс события, собираем там ивенты и готовим к обработке */
    }
}
