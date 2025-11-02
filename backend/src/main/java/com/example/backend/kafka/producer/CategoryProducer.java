package com.example.backend.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.entity.Category;
import com.example.backend.kafka.dto.AuditEvent;
import com.example.backend.kafka.dto.CategoryEvent;
import com.example.backend.kafka.enums.CRUDType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryProducer {
    private static final Logger logger = LoggerFactory.getLogger(AccountProducer.class);
    
    private final AuditProducer auditProducer;
    private final KafkaTemplate<String, CategoryEvent> categoryTemplate;

    public CategoryEvent buildFromCategory(CRUDType eventType, Category category)
    {
        AuditEvent auditEvent = auditProducer.buildAuditEvent(eventType);
        return new CategoryEvent(auditEvent, 
            category.getId(), category.getName(), category.getDescription());
    }

    public CategoryEvent builCategoryEvent(CRUDType eventType, Integer categoryId)
    {
        AuditEvent auditEvent = auditProducer.buildAuditEvent(eventType);
        return new CategoryEvent(auditEvent, 
            categoryId, null, null);
    }

    public void sendCategory(CategoryEvent event) {
        categoryTemplate.send("category-events", event).whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error("Failed to send CategoryEvent [{}] to topic [{}]: {}",
                        event, "category-events", ex.getMessage(), ex);
            } else {
                logger.info("Sent CategoryEvent [{}] to topic [{}] with offset {}, partition {}, timestamp {}",
                        event, "category-events",
                        result.getRecordMetadata().offset(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().timestamp());
            }
        });
    }

}
