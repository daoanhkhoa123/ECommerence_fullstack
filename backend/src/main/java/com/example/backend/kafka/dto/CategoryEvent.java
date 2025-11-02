package com.example.backend.kafka.dto;

public record CategoryEvent(
    AuditEvent auditEvent,
    
    Integer categoryId,
    String name,
    String description
) {}
