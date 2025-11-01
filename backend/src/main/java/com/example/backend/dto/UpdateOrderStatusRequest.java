package com.example.backend.dto;

import com.example.backend.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(
    @NotNull(message = "Order status is required")
    OrderStatus status
) {}