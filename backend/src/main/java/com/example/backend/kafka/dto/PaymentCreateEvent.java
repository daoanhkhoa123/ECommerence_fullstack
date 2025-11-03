package com.example.backend.kafka.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.backend.enums.OrderStatus;
import com.example.backend.enums.PaymentMethod;

public record PaymentCreateEvent(
    Integer paymentId,
    Integer orderId,
    Integer actorId,

    // Payment
    PaymentMethod paymentMethod,
    OrderStatus paymentStatus,
    BigDecimal paidAmount,
    String transactionRef,
    LocalDateTime paidAt) {}
