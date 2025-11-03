package com.example.backend.kafka.dto;

public record OrderItemDeleteEvent(
    Integer actorId,
    Integer orderItemId
) {}
