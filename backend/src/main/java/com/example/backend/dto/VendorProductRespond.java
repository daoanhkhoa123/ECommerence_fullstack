package com.example.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record VendorProductRespond(
    Integer vendorProductId,
    Integer productId,
    Integer vendorId,

    // Product
    String name,
    String description,
    String brand,
    String imageUrl,

    // Vendor Product
    BigDecimal price,
    Integer stock,
    String sku,
    @NotNull Boolean isFeatured
) {}