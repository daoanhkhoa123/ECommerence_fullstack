package com.example.backend.kafka.dto;

public record VendorProductReadEvent(
    Integer actorId,
    Integer vendorId
) {}
