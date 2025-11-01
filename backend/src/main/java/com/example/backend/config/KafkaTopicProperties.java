package com.example.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.kafka.topic")
public class KafkaTopicProperties {
    private String auditLog;
    private String orderEvents;
    private String productEvents;

    public String getAuditLog() { return auditLog; }
    public void setAuditLog(String auditLog) { this.auditLog = auditLog; }

    public String getOrderEvents() { return orderEvents; }
    public void setOrderEvents(String orderEvents) { this.orderEvents = orderEvents; }

    public String getProductEvents() { return productEvents; }
    public void setProductEvents(String productEvents) { this.productEvents = productEvents; }
}