package com.example.backend.application;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.CategoryRequestRespond;
import com.example.backend.dto.ProductCategoryPatchRequest;
import com.example.backend.entity.Category;
import com.example.backend.service.CategoryService;
import com.example.backend.service.ProductCategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryApplication {
    private final CategoryService categoryService;
    private final ProductCategoryService productCategoryService;

    private CategoryRequestRespond buildFromCategory(Category category)
    {
        return new CategoryRequestRespond(category.getName(), 
        category.getDescription(), category.getId());
    }


    public CategoryRequestRespond findCategoryById(Integer id)
    {
        Category category = categoryService.findCategoryById(id);
        return buildFromCategory(category);
    }

    public List<CategoryRequestRespond> findAllByProductId(Integer productId)
    {
        return categoryService.findAllByProductId(productId).stream()
        .map(this::buildFromCategory).toList();
    }


    public CategoryRequestRespond createCategory(CategoryRequestRespond request)
    {
        Category category = categoryService.createCategory(request);
        return buildFromCategory(category);
    }

    public CategoryRequestRespond updateCategory(Integer categoryId, CategoryRequestRespond request)
    {
        Category category = categoryService.updateCategory(categoryId, request);
        return buildFromCategory(category);
    }

    public void deleteCategorById(Integer id)
    {
        deleteCategorById(id);
    }

    public List<CategoryRequestRespond> patchProductCategory(Integer productId, ProductCategoryPatchRequest request)
    {
        if ((request.addCategoryIds() == null || request.addCategoryIds().isEmpty()) &&
            (request.remCategoryIds() == null || request.remCategoryIds().isEmpty())) 
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "At least one of addCategoryIds or remCategoryIds must be provided"
            );

        // Check intersection
        if (request.addCategoryIds() != null && request.remCategoryIds() != null &&
            request.addCategoryIds().stream().anyMatch(request.remCategoryIds()::contains)) 
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "addCategoryIds and remCategoryIds must not overlap"
            );

        productCategoryService.addProductCategory(productId, request.addCategoryIds());
        productCategoryService.removeProductCategory(productId, request.remCategoryIds());

        return findAllByProductId(productId);
    }
}
