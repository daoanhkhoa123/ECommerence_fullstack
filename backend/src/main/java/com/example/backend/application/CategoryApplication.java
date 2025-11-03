package com.example.backend.application;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.config.JwtService;
import com.example.backend.dto.CategoryRequestRespond;
import com.example.backend.dto.ProductCategoryPatchRequest;
import com.example.backend.entity.Category;
import com.example.backend.kafka.dto.CategoryCreateUpdateEvent;
import com.example.backend.kafka.dto.CategoryReadDeleteEvent;
import com.example.backend.kafka.producer.CategoryProducer;
import com.example.backend.service.CategoryService;
import com.example.backend.service.ProductCategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryApplication {

    private final CategoryProducer categoryProducer;
    private final CategoryService categoryService;
    private final ProductCategoryService productCategoryService;
    private final JwtService jwtService;

    private CategoryRequestRespond buildFromCategory(Category category) {
        return new CategoryRequestRespond(
            category.getName(),
            category.getDescription(),
            category.getId()
        );
    }

    public CategoryRequestRespond findCategoryById(Integer id) {
        Category category = categoryService.findCategoryById(id);
        return buildFromCategory(category);
    }

    public List<CategoryRequestRespond> findAllByProductId(Integer productId) {
        return categoryService.findAllByProductId(productId).stream()
            .map(this::buildFromCategory)
            .toList();
    }

    public CategoryRequestRespond createCategory(CategoryRequestRespond request) {
        Category category = categoryService.createCategory(request);

        Integer actorId = jwtService.getCurrentUserId();
        CategoryCreateUpdateEvent event = categoryProducer.buildCategoryCreateUpdate(actorId, category);
        // manually override actorId if needed
        categoryProducer.sendCategoryCreated(event);

        return buildFromCategory(category);
    }

    public CategoryRequestRespond updateCategory(Integer categoryId, CategoryRequestRespond request) {
        Category category = categoryService.updateCategory(categoryId, request);

        Integer actorId = jwtService.getCurrentUserId();
        CategoryCreateUpdateEvent event = categoryProducer.buildCategoryCreateUpdate(actorId, category);
        categoryProducer.sendCategoryUpdated(event);

        return buildFromCategory(category);
    }

    public void deleteCategoryById(Integer id) {
        // emit before deletion (entity still exists)
        Integer actorId = jwtService.getCurrentUserId();
        CategoryReadDeleteEvent event = categoryProducer.buildCategoryReadDelete(actorId, id);
        categoryProducer.sendCategoryDeleted(event);

        categoryService.deleteCategoryById(id);
    }

    // ---------- PATCH PRODUCT CATEGORY ----------
    public List<CategoryRequestRespond> patchProductCategory(Integer productId, ProductCategoryPatchRequest request) {
        if ((request.addCategoryIds() == null || request.addCategoryIds().isEmpty()) &&
            (request.remCategoryIds() == null || request.remCategoryIds().isEmpty())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "At least one of addCategoryIds or remCategoryIds must be provided"
            );
        }

        // Prevent overlapping category IDs
        if (request.addCategoryIds() != null && request.remCategoryIds() != null &&
            request.addCategoryIds().stream().anyMatch(request.remCategoryIds()::contains)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "addCategoryIds and remCategoryIds must not overlap"
            );
        }

        Integer actorId = jwtService.getCurrentUserId();
        productCategoryService.addProductCategory(actorId, request.addCategoryIds());
        categoryProducer.sendProductCategoryCreated(productId, productId, request.addCategoryIds());

        productCategoryService.removeProductCategory(productId, request.remCategoryIds());
        categoryProducer.sendProductCategoryDeleted(actorId, productId, request.remCategoryIds());            

        return findAllByProductId(productId);
    }
}
