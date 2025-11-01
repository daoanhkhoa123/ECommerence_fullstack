package com.example.backend.dto;

import java.math.BigDecimal;

public record OrderItemProductRespond(
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
    String productBrand,
    String productImg
) {}
