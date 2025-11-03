package com.example.backend.kafka.dto;

public record CategoryReadDeleteEvent(
    Integer actorId,

    Integer categoryId
) {}
