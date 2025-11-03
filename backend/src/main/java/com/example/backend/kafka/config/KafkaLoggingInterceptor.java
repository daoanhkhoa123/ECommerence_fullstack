package com.example.backend.kafka.config;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaLoggingInterceptor implements ProducerInterceptor<String, Object>{

    private static final Logger logger = LoggerFactory.getLogger(KafkaLoggingInterceptor.class);

    @Override
    public ProducerRecord<String, Object> onSend(ProducerRecord<String, Object> record)
    {
        logger.info("[PRODUCER] topic={}, key={}, partition={}, value={}",
                 record.topic(), record.key(), record.partition(), record.value());

        return record;
    }


    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception)
    {
        if (exception!=null) 
            logger.error("[PRODUCER-ACK] Failed to send to topic={}, error={}", metadata.topic(), exception.getMessage());
        else
            logger.debug("[PRODUCER-ACK] Sent successfully topic={}, offset={}", metadata.topic(), metadata.offset());
    }

    @Override
    public void close(){}

    @Override
    public void configure(Map<String, ?> configs) {}
}
