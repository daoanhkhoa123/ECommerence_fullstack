package com.example.backend.dto;

import java.math.BigDecimal;

public record OrderItemFullDto(
    Integer orderItemId,
    Integer quantity,
    BigDecimal price,
    BigDecimal subtotal,
    Integer productId,
    String name,
    String description,
    String brand,
    String category,
    BigDecimal vendorProductPrice,
    Integer vendorId,
    String shopName,
    String vendorDescription
) {}
