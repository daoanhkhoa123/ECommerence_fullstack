package com.example.backend.kafka.dto;

public record VendorReadDeleteEvent(
    Integer actorId,    

    Integer vendorId
) {}
