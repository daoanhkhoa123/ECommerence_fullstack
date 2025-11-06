package com.example.backend.kafka.config;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaLoggingInterceptor implements ProducerInterceptor<String, Object> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaLoggingInterceptor.class);

    @Override
    public ProducerRecord<String, Object> onSend(ProducerRecord<String, Object> record) {
        logger.info("[KAFKA PRODUCER] Sending -> topic={}, key={}, partition={}, value={}",
                record.topic(), record.key(), record.partition(), record.value());
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            logger.error("[KAFKA PRODUCER ACK] Failed to send -> topic={}, error={}",
                    metadata != null ? metadata.topic() : "unknown",
                    exception.getMessage());
        } else if (metadata != null) {
            logger.debug("[KAFKA PRODUCER ACK] Sent successfully -> topic={}, partition={}, offset={}",
                    metadata.topic(), metadata.partition(), metadata.offset());
        }
    }

    @Override
    public void close() {
        // nothing to close
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // no custom configuration
    }
}
