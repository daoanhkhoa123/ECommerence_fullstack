package com.example.backend.kafka.dto;

import java.time.LocalDate;

public record CustomerEvent(
    AuditEvent auditEvent,    

    Integer customerId,
    String email,
    String fullName,
    String phone,
    String adress,
    LocalDate birDate
) {}

