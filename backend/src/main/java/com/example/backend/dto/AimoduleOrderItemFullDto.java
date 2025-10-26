package com.example.backend.dto;

import java.math.BigDecimal;

public record AimoduleOrderItemFullDto(
        Integer orderItemId,
        Integer quantity,
        BigDecimal price,
        BigDecimal subtotal,
        Integer productId,
        String productName,
        String productDescription,
        String productBrand,
        String productCategory,
        BigDecimal vendorPrice,
        Integer vendorId,
        String vendorShopName,
        String vendorDescription
) {}
