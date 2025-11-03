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

    private final KafkaTemplate<String, CategoryCreateUpdateEvent> categoryCreateUpdateTemplate;
    private final KafkaTemplate<String, CategoryReadDeleteEvent> categoryReadDeleteTemplate;
    private final KafkaTemplate<String, ProductCategoryCreateDeleteEvent> productCategoryCreateDeleteTemplate;

    // -------- BUILDERS --------

    public CategoryCreateUpdateEvent buildCategoryCreateUpdate(Integer actorId, Category category) {
        return new CategoryCreateUpdateEvent(
            actorId,
            category.getId(),
            category.getName(),
            category.getDescription()
        );
    }

    public CategoryReadDeleteEvent buildCategoryReadDelete(Integer actorId, Integer categoryId) {
        return new CategoryReadDeleteEvent(
            actorId,
            categoryId
        );
    }

    // -------- SENDERS --------

    public void sendCategoryCreated(CategoryCreateUpdateEvent event) {
        categoryCreateUpdateTemplate.send(KafkaTopic.CATEGORY_CREATE.getName(), event);
    }

    public void sendCategoryUpdated(CategoryCreateUpdateEvent event) {
        categoryCreateUpdateTemplate.send(KafkaTopic.CATEGORY_UPDATE.getName(), event);
    }

    public void sendCategoryRead(CategoryReadDeleteEvent event) {
        categoryReadDeleteTemplate.send(KafkaTopic.CATEGORY_READ.getName(), event);
    }

    public void sendCategoryDeleted(CategoryReadDeleteEvent event) {
        categoryReadDeleteTemplate.send(KafkaTopic.CATEGORY_DELETE.getName(), event);
    }

    public void sendProductCategoryCreated(Integer actorId, Integer productId, List<Integer> categoryIds)
    {
        ProductCategoryCreateDeleteEvent event = new ProductCategoryCreateDeleteEvent(
            actorId, productId, categoryIds);

        productCategoryCreateDeleteTemplate.send(KafkaTopic.PRODUCT_CATEGORY_CREATE.getName(), event);
    }

    public void sendProductCategoryDeleted(Integer actorId, Integer productId, List<Integer> categoryIds)
    {
        ProductCategoryCreateDeleteEvent event = new ProductCategoryCreateDeleteEvent(
            actorId, productId, categoryIds);

        productCategoryCreateDeleteTemplate.send(KafkaTopic.PRODUCT_CATEGORY_DELETE.getName(), event);
    }
    
}
