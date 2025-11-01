package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.application.CategoryApplication;
import com.example.backend.dto.CategoryRequestRespond;
import com.example.backend.dto.ProductCategoryPatchRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryApplication categoryApplication;
    
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryRequestRespond> getCategory(Integer categoryId)
    {
        CategoryRequestRespond respond = categoryApplication.findCategoryById(categoryId);
        return ResponseEntity.ok(respond);
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryRequestRespond> createCategory(
            @Valid @RequestBody CategoryRequestRespond request) {
        CategoryRequestRespond response = categoryApplication.createCategory(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update/{categoryId}")
    public ResponseEntity<CategoryRequestRespond> updateCategory(
            @PathVariable Integer categoryId,
            @Valid @RequestBody CategoryRequestRespond request) {
        CategoryRequestRespond response = categoryApplication.updateCategory(categoryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer categoryId) {
        categoryApplication.deleteCategorById(categoryId);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CategoryRequestRespond>> getProductCategories(@RequestParam Integer productId) {
        List<CategoryRequestRespond> respond = categoryApplication.findAllByProductId(productId);
        return ResponseEntity.ok(respond);
    }

    @PatchMapping("/product/{productId}")
    public ResponseEntity<List<CategoryRequestRespond>> patchProductCategories(
            @PathVariable Integer productId,
            @Valid @RequestBody ProductCategoryPatchRequest request) {

        List<CategoryRequestRespond> response = categoryApplication.patchProductCategory(productId, request);
        return ResponseEntity.ok(response);
    }  
}
