package com.example.backend.kafka.dto;

public record VendorEvent(
    AuditEvent auditEvent,    

    Integer vendorId,
    String email,
    String shopName,
    String description,
    String phone
) {}
