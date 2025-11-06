package com.example.backend.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.CategoryRequestRespond;
import com.example.backend.dto.ProductCategoryPatchRequest;
import com.example.backend.entity.Category;
import com.example.backend.kafka.producer.CategoryProducer;
import com.example.backend.security.JwtService;
import com.example.backend.service.CategoryService;
import com.example.backend.service.ProductCategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryApplication {

    private static final Logger log = LoggerFactory.getLogger(CategoryApplication.class);

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

    // ---------- FIND ----------
    public CategoryRequestRespond findCategoryById(Integer id) {
        log.info("Fetching category by id={}", id);
        Category category = categoryService.findCategoryById(id);
        log.info("Called categoryService.findCategoryById() - Found id={}, name={}", category.getId(), category.getName());
        return buildFromCategory(category);
    }

    public List<CategoryRequestRespond> findAllByProductId(Integer productId) {
        log.info("Fetching categories for productId={}", productId);
        List<Category> categories = categoryService.findAllByProductId(productId);
        log.info("Called categoryService.findAllByProductId() - Returned {} categories for productId={}", categories.size(), productId);
        return categories.stream().map(this::buildFromCategory).toList();
    }

    // ---------- CREATE ----------
    public CategoryRequestRespond createCategory(CategoryRequestRespond request) {
        log.info("Creating category with name={}", request.name());
        Category category = categoryService.createCategory(request);
        log.info("Called categoryService.createCategory() - Created id={}, name={}", category.getId(), category.getName());

        Integer actorId = jwtService.getCurrentUserId();
        log.info("Retrieved actorId={} from jwtService", actorId);

        categoryProducer.sendCategoryCreated(actorId, category);
        log.info("Called categoryProducer.sendCategoryCreated() - Sent event for category id={}", category.getId());

        return buildFromCategory(category);
    }

    // ---------- UPDATE ----------
    public CategoryRequestRespond updateCategory(Integer categoryId, CategoryRequestRespond request) {
        log.info("Updating category id={}", categoryId);
        Category category = categoryService.updateCategory(categoryId, request);
        log.info("Called categoryService.updateCategory() - Updated id={}, new name={}", category.getId(), category.getName());

        Integer actorId = jwtService.getCurrentUserId();
        log.info("Retrieved actorId={} from jwtService", actorId);

        categoryProducer.sendCategoryUpdated(actorId, category);
        log.info("Called categoryProducer.sendCategoryUpdated() - Sent event for category id={}", category.getId());

        return buildFromCategory(category);
    }

    // ---------- DELETE ----------
    public void deleteCategoryById(Integer id) {
        log.info("Deleting category id={}", id);
        Integer actorId = jwtService.getCurrentUserId();
        log.info("Retrieved actorId={} from jwtService", actorId);

        categoryProducer.sendCategoryDeleted(actorId, id);
        log.info("Called categoryProducer.sendCategoryDeleted() - Sent event for category id={}", id);

        categoryService.deleteCategoryById(id);
        log.info("Called categoryService.deleteCategoryById() - Deleted category id={}", id);
    }

    // ---------- PATCH ----------
    public List<CategoryRequestRespond> patchProductCategory(Integer productId, ProductCategoryPatchRequest request) {
        log.info("Patching product-category for productId={}", productId);

        if ((request.addCategoryIds() == null || request.addCategoryIds().isEmpty()) &&
            (request.remCategoryIds() == null || request.remCategoryIds().isEmpty())) {
            log.warn("Invalid patch request - no add/remove IDs for productId={}", productId);
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "At least one of addCategoryIds or remCategoryIds must be provided"
            );
        }

        if (request.addCategoryIds() != null && request.remCategoryIds() != null &&
            request.addCategoryIds().stream().anyMatch(request.remCategoryIds()::contains)) {
            log.warn("Invalid patch request - overlapping add/rem IDs for productId={}", productId);
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "addCategoryIds and remCategoryIds must not overlap"
            );
        }

        Integer actorId = jwtService.getCurrentUserId();
        log.info("Retrieved actorId={} from jwtService", actorId);

        if (request.addCategoryIds() != null && !request.addCategoryIds().isEmpty()) {
            log.info("Adding categories {} to productId={}", request.addCategoryIds(), productId);
            productCategoryService.addProductCategory(productId, request.addCategoryIds());
            log.info("Called productCategoryService.addProductCategory() - Added categories to productId={}", productId);

            categoryProducer.sendProductCategoryCreated(actorId, productId, request.addCategoryIds());
            log.info("Called categoryProducer.sendProductCategoryCreated() - Sent event for productId={}", productId);
        }

        if (request.remCategoryIds() != null && !request.remCategoryIds().isEmpty()) {
            log.info("Removing categories {} from productId={}", request.remCategoryIds(), productId);
            productCategoryService.removeProductCategory(productId, request.remCategoryIds());
            log.info("Called productCategoryService.removeProductCategory() - Removed categories from productId={}", productId);

            categoryProducer.sendProductCategoryDeleted(actorId, productId, request.remCategoryIds());
            log.info("Called categoryProducer.sendProductCategoryDeleted() - Sent event for productId={}", productId);
        }

        List<CategoryRequestRespond> updatedCategories = findAllByProductId(productId);
        log.info("After patch, productId={} has {} categories", productId, updatedCategories.size());

        return updatedCategories;
    }
}
