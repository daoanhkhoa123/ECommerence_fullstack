package com.example.backend.kafka.dto;

import com.example.backend.kafka.enums.CRUDType;

public record  AuditEvent(
    CRUDType eventType,

    // who did this
    Integer accId
) {}
