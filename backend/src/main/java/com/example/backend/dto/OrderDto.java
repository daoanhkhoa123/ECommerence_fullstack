package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDto(
    Integer orderId,
    Integer customerId,
    String orderStatus,
    BigDecimal totalAmount,
    String shippingAddress,
    LocalDateTime orderTime
) {}
