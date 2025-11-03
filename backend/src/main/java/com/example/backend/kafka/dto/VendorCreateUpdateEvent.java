package com.example.backend.kafka.dto;

public record VendorCreateUpdateEvent(
    Integer actorId,    

    Integer vendorId,
    String email,
    String shopName,
    String description,
    String phone
) {}
