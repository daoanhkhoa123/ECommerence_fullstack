package com.example.backend.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.core.KafkaTemplate;
import org.apache.kafka.common.TopicPartition;

@Configuration
public class KafkaConsumerErrorHandlerConfig {
    
    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {
        // Dead-letter recoverer
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template,
            (record, ex) -> new TopicPartition(record.topic() + ".DLT", record.partition()));

        // Retry up to 2 times (total attempts = 3)
        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(2);
        backOff.setInitialInterval(1000L);
        backOff.setMultiplier(2.0);
        backOff.setMaxInterval(10000L);

        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, backOff);

        // **Disable seeking**; let the handler keep track and stop retrying after max
        handler.setSeekAfterError(false);

        return handler;
    }

}
