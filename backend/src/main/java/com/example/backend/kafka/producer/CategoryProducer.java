package com.example.backend.kafka.producer;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.entity.Category;
import com.example.backend.kafka.dto.CategoryCreateUpdateEvent;
import com.example.backend.kafka.dto.CategoryReadDeleteEvent;
import com.example.backend.kafka.dto.ProductCategoryCreateDeleteEvent;
import com.example.backend.kafka.enums.KafkaTopic;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryProducer {

    // ✅ Only one template for all event types
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // -------- BUILDERS --------

    private CategoryCreateUpdateEvent buildCategoryCreateUpdate(Integer actorId, Category category) {
        return new CategoryCreateUpdateEvent(
            actorId,
            category.getId(),
            category.getName(),
            category.getDescription()
        );
    }

    private CategoryReadDeleteEvent buildCategoryReadDelete(Integer actorId, Integer categoryId) {
        return new CategoryReadDeleteEvent(actorId, categoryId);
    }

    // -------- SENDERS --------

    public void sendCategoryCreated(Integer actorId, Category category) {
        kafkaTemplate.send(KafkaTopic.CATEGORY_CREATE.getName(),
            buildCategoryCreateUpdate(actorId, category));
    }

    public void sendCategoryUpdated(Integer actorId, Category category) {
        kafkaTemplate.send(KafkaTopic.CATEGORY_UPDATE.getName(),
            buildCategoryCreateUpdate(actorId, category));
    }

    public void sendCategoryRead(Integer actorId, Integer categoryId) {
        kafkaTemplate.send(KafkaTopic.CATEGORY_READ.getName(),
            buildCategoryReadDelete(actorId, categoryId));
    }

    public void sendCategoryDeleted(Integer actorId, Integer categoryId) {
        kafkaTemplate.send(KafkaTopic.CATEGORY_DELETE.getName(),
            buildCategoryReadDelete(actorId, categoryId));
    }

    public void sendProductCategoryCreated(Integer actorId, Integer productId, List<Integer> categoryIds) {
        kafkaTemplate.send(KafkaTopic.PRODUCT_CATEGORY_CREATE.getName(),
            new ProductCategoryCreateDeleteEvent(actorId, productId, categoryIds));
    }

    public void sendProductCategoryDeleted(Integer actorId, Integer productId, List<Integer> categoryIds) {
        kafkaTemplate.send(KafkaTopic.PRODUCT_CATEGORY_DELETE.getName(),
            new ProductCategoryCreateDeleteEvent(actorId, productId, categoryIds));
    }
}
