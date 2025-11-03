package com.example.backend.kafka.dto;

import com.example.backend.enums.OrderStatus;

public record OrderStatusUpdateEvent(
    Integer actorId,
    Integer orderId,
    OrderStatus orderStatus) {}
