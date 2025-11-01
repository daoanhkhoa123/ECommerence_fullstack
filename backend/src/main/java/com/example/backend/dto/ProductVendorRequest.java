package com.example.backend.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductVendorRequest(
    // Product
    @NotNull(message = "Product name is required")
    String name,
    String description,
    String brand,
    String imageUrl,

    // Category
    List<Integer> categoryIds,

    // Vendor Product
    @NotNull(message = "Price is required")
    BigDecimal price,
    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    Integer stock,

    String sku,
    @NotNull Boolean isFeatured
) {}