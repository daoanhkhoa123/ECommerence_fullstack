package com.example.backend.service;

import com.example.backend.config.KafkaConfig;
import com.example.backend.dto.AuditLogEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class KafkaLoggingService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaLoggingService.class);
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfig kafkaConfig;
    private final ObjectMapper objectMapper;

    public KafkaLoggingService(
            KafkaTemplate<String, String> kafkaTemplate,
            KafkaConfig kafkaConfig,
            ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConfig = kafkaConfig;
        this.objectMapper = objectMapper;
    }

    public void logAudit(String eventType, String entityType, Integer entityId, 
                        String action, String username, String details) {
        try {
            AuditLogEvent event = new AuditLogEvent(
                eventType,
                entityType,
                entityId,
                action,
                username,
                LocalDateTime.now(),
                details
            );

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(kafkaConfig.getAuditLogTopic(), message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        logger.error("Failed to send audit log to Kafka", ex);
                    } else {
                        logger.debug("Audit log sent successfully: {}", message);
                    }
                });
        } catch (Exception e) {
            logger.error("Error creating audit log", e);
        }
    }

    public void logOrderEvent(String orderId, String eventType, String details) {
        try {
            String message = objectMapper.writeValueAsString(Map.of(
                "orderId", orderId,
                "eventType", eventType,
                "details", details,
                "timestamp", LocalDateTime.now()
            ));
            
            kafkaTemplate.send(kafkaConfig.getOrderEventsTopic(), orderId, message);
        } catch (Exception e) {
            logger.error("Error logging order event", e);
        }
    }

    public void logProductEvent(String productId, String eventType, String details) {
        try {
            String message = objectMapper.writeValueAsString(Map.of(
                "productId", productId,
                "eventType", eventType,
                "details", details,
                "timestamp", LocalDateTime.now()
            ));
            
            kafkaTemplate.send(kafkaConfig.getProductEventsTopic(), productId, message);
        } catch (Exception e) {
            logger.error("Error logging product event", e);
        }
    }
}