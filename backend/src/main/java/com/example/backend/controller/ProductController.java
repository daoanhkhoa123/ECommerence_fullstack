package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.entity.Product;
import com.example.backend.service.CategoryService;
import com.example.backend.service.ProductService;
import com.example.backend.service.VendorProductService;

import com.example.backend.dto.VendorProductRequest;
import com.example.backend.dto.ProductCategoryPatchRequest;
import com.example.backend.dto.ProductVendorRequest;
import com.example.backend.dto.VendorProductRespond;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final VendorProductService vendorProductService;

    @GetMapping("/vendor-product/{vendorId}")
    public ResponseEntity<List<VendorProductRespond>> getVendorProduct(
        @PathVariable Integer vendorId) {
        return ResponseEntity.ok(vendorProductService.findByVendorId(vendorId,
        categoryService::findAllByProduct));
    }

    @PostMapping("/vendor/{vendorId}")
    public ResponseEntity<VendorProductRespond> createVendorProduct(
            @PathVariable Integer vendorId,
            @Valid @RequestBody VendorProductRequest request) {

        Product product = productService.createProduct(request);
        categoryService.addProductCategory(product.getId(), request.categoryRequest().addCategoryIds());
        VendorProductRespond respond = vendorProductService.createVendorProduct(vendorId, product, request, categoryService::findAllByProduct);
        return ResponseEntity.ok(respond);
    }

    @PatchMapping("/vendor-product/{vendorProductId}")
    public ResponseEntity<VendorProductRespond> updateVendorProduct(
            @PathVariable Integer vendorProductId,
            @Valid @RequestBody ProductVendorRequest request) {
        
        Product product = productService.findProductByVendorProductId(vendorProductId);
        categoryService.patchProductCategory(product.getId(), new ProductCategoryPatchRequest(request.categoryIds(), null));
        VendorProductRespond respond = vendorProductService.updateVendorProduct(vendorProductId, product, request, categoryService::findAllByProduct);
        return ResponseEntity.ok(respond);
    }

    @DeleteMapping("/vendor-product/{vendorProductId}")
    public ResponseEntity<Void> deleteVendorProduct(@PathVariable Integer vendorProductId) {
        vendorProductService.deleteVendorProduct(vendorProductId);
        return ResponseEntity.noContent().build();
    }
}
