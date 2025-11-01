package com.example.backend.dto;

import java.math.BigDecimal;
import com.example.backend.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
    @NotNull(message = "Payment method is required")
    PaymentMethod paymentMethod,

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    BigDecimal paidAmount,

    String transactionRef
) {}
