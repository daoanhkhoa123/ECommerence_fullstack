package com.example.backend.kafka.dto;

public record CustomerReadDeleteEvent(
    Integer actorId,
    Integer customerId
) {}
