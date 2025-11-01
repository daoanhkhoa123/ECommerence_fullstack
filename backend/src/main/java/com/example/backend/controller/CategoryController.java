package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.CategoryRequestRespond;
import com.example.backend.dto.ProductCategoryPatchRequest;
import com.example.backend.dto.ProductCategoryPatchRespond;
import com.example.backend.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;
    
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryRequestRespond> getCategory(Integer categoryId)
    {
        CategoryRequestRespond respond = categoryService.findCategoryById(categoryId);
        return ResponseEntity.ok(respond);
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryRequestRespond> createCategory(
            @Valid @RequestBody CategoryRequestRespond request) {
        CategoryRequestRespond response = categoryService.createCategory(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update/{categoryId}")
    public ResponseEntity<CategoryRequestRespond> updateCategory(
            @PathVariable Integer categoryId,
            @Valid @RequestBody CategoryRequestRespond request) {
        CategoryRequestRespond response = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductCategoryPatchRespond> getProductCategories(@RequestParam Integer productId) {
        List<CategoryRequestRespond> catdots = categoryService.findAllByProduct(productId);
        ProductCategoryPatchRespond respond = new ProductCategoryPatchRespond(productId, catdots);
        return ResponseEntity.ok(respond);
    }

    @PatchMapping("/product/{productId}")
    public ResponseEntity<ProductCategoryPatchRespond> patchProductCategories(
            @PathVariable Integer productId,
            @Valid @RequestBody ProductCategoryPatchRequest request) {

        ProductCategoryPatchRespond response = categoryService.patchProductCategory(productId, request);
        return ResponseEntity.ok(response);
    }

    

}
