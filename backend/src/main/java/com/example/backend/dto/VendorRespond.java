package com.example.backend.dto;

public record VendorRespond(
    Integer vendorId,
    String email,
    String shopName,
    String description,
    String phone
) {}
