package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.backend.enums.OrderStatus;

public record OrderRespond(
    Integer orderId,
    Integer customerId,
    OrderStatus orderStatus,
    BigDecimal totalAmount,
    String shippingAddress,
    LocalDateTime orderTime
) {}
