package com.example.backend.kafka.dto;

import java.math.BigDecimal;

public record VendorProductCreateUpdateEvent(
    Integer actorId,
    String name,
    String description,
    String brand,
    
    BigDecimal price,
    Integer stock,
    String sku,
    Boolean isFeatured
) {}
