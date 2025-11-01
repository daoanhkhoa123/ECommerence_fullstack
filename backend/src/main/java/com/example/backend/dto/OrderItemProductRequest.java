package com.example.backend.dto;

import jakarta.validation.constraints.NotNull;

public record OrderItemProductRequest(
    @NotNull Integer vendorProductId,
    @NotNull Integer quantity
) {}
