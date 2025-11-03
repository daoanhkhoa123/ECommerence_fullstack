package com.example.backend.kafka.dto;

public record VendorProductDeleteEvent(
    Integer actorId,
    Integer vendorProductId
) {}
