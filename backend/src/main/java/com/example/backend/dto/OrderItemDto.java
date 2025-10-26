package com.example.backend.dto;

import java.math.BigDecimal;

public record OrderItemDto(
    Integer orderItemId,
    Integer orderId,
    Integer vendorProductId,
    Integer quantity,
    BigDecimal price,
    BigDecimal subtotal
) {}
