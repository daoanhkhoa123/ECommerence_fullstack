package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.VendorProductRequest;
import com.example.backend.application.ProductApplication;
import com.example.backend.dto.ProductVendorRequest;
import com.example.backend.dto.VendorProductRespond;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductApplication productApplication;

    @GetMapping("/vendor-product/{vendorId}")
    public ResponseEntity<List<VendorProductRespond>> getVendorProduct(
        @PathVariable Integer vendorId) {
        return ResponseEntity.ok(productApplication.findByVendorId(vendorId));
    }

    @PostMapping("/vendor/{vendorId}")
    public ResponseEntity<VendorProductRespond> createVendorProduct(
            @PathVariable Integer vendorId,
            @Valid @RequestBody VendorProductRequest request) {

        VendorProductRespond respond = productApplication.createVendorProduct(vendorId, request);
        return ResponseEntity.ok(respond);
    }

    @PatchMapping("/vendor-product/{vendorProductId}")
    public ResponseEntity<VendorProductRespond> updateVendorProduct(
            @PathVariable Integer vendorProductId,
            @Valid @RequestBody ProductVendorRequest request) {
        
        VendorProductRespond respond = productApplication.updateVendorProduct(vendorProductId, request);
        return ResponseEntity.ok(respond);
    }

    @DeleteMapping("/vendor-product/{vendorProductId}")
    public ResponseEntity<Void> deleteVendorProduct(@PathVariable Integer vendorProductId) {
        productApplication.deleteVendorProduct(vendorProductId);
        return ResponseEntity.noContent().build();
    }
}
