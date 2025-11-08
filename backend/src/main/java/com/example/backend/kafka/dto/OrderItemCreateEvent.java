package com.example.backend.kafka.dto;

import java.math.BigDecimal;

public record OrderItemCreateEvent(
    Integer actorId,

    Integer orderId,
    Integer orderItemId,
    Integer vendorProductId,

    // Order Item 
    Integer quantity,
    BigDecimal subTotal,

    // Vendor
    String shopName,
    String shopPhone,

    // Product
    String productName,
    String productBrand
) {}
