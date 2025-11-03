package com.example.backend.kafka.dto;

public record CategoryCreateUpdateEvent(
    Integer actorId,

    Integer categoryId,
    String name,
    String description
) {}
