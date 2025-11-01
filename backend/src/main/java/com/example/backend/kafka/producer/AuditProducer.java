package com.example.backend.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.kafka.dto.AuditEvent;
import com.example.backend.kafka.enums.AuditTopic;

@Service
public class AuditProducer {
    private static final Logger logger = LoggerFactory.getLogger(AuditProducer.class);
    private final KafkaTemplate<String, AuditEvent> auditTemplate;
    
    public AuditProducer(KafkaTemplate<String, AuditEvent> auditTemplate)
    {
        this.auditTemplate = auditTemplate;
    }

    public void send(AuditTopic topic, AuditEvent event) {
        auditTemplate.send(topic.getName(), event).whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error("Failed to send event [{}] to topic [{}]: {}", 
                    event, topic.getName(), ex.getMessage(), ex);
            } else {
                logger.info("Sent event [{}] to topic [{}] with offset {}, partition {}, timestamp {}",
                    event, topic.getName(),
                    result.getRecordMetadata().offset(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().timestamp());
            }
        });
    }
}
