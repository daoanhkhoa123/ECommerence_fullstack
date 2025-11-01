package com.example.backend.kafka.dto;

import com.example.backend.kafka.enums.CUDType;

public record VendorEvent(
    CUDType eventType,
    Integer vendorId,

    String email,
    String shopName,
    String description,
    String phone
) {}
