package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.backend.enums.OrderStatus;
import com.example.backend.enums.PaymentMethod;

public record PaymentRespond(
    Integer paymentId,
    Integer orderId,
    Integer customerId,

    // Payment
    PaymentMethod paymentMethod,
    OrderStatus paymentStatus,
    BigDecimal paidAmount,
    String transactionRef,
    LocalDateTime paidAt
) {}
