package com.example.backend.kafka.dto;

import com.example.backend.kafka.enums.CRUDType;

public record VendorEvent(
    CRUDType eventType,
    Integer vendorId,

    String email,
    String shopName,
    String description,
    String phone
) {}
