package cqrs_example.queryservice.config;

import cqrs_example.kafkacore.constants.CqrsCoreConstants;
import cqrs_example.queryservice.exception.NonRetryableException;
import cqrs_example.queryservice.exception.RetryableException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfiguration {

    private final Environment environment;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.consumer.bootstrap-servers"));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, CqrsCoreConstants.GROUP_ID);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

//        props.put((JsonDeserializer.TYPE_MAPPINGS), CustomerCreateRequestEvent.class.getCanonicalName() + ":springcloudms.customerservice.events.CustomerCreateRequestEvent");
//        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, CustomerCreateRequestEvent.class.getCanonicalName());
//        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "springcloudms.*");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "cqrs_example.*");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> customerConsumerFactory,
            KafkaTemplate<String, Object> kafkaTemplate) {

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplate),
                new FixedBackOff(3000, 3));

        errorHandler.addRetryableExceptions(RetryableException.class);
        errorHandler.addNotRetryableExceptions(NonRetryableException.class);

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(customerConsumerFactory);
        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }

    @Bean
    KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    ProducerFactory<String, Object> producerFactory() {

        Map<String, Object> producerConfig = new HashMap<>();

        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.consumer.bootstrap-servers"));
        producerConfig.put(ProducerConfig.RETRIES_CONFIG, 0);
        producerConfig.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        producerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(producerConfig);
    }
}