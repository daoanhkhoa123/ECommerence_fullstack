package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {
    
    private final KafkaTopicProperties topicProperties;

    public KafkaConfig(KafkaTopicProperties topicProperties) {
        this.topicProperties = topicProperties;
    }

    public String getAuditLogTopic() {
        return topicProperties.getAuditLog();
    }

    public String getOrderEventsTopic() {
        return topicProperties.getOrderEvents();
    }

    public String getProductEventsTopic() {
        return topicProperties.getProductEvents();
    }
}