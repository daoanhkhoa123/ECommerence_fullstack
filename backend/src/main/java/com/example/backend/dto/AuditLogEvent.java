package com.example.backend.dto;

import java.time.LocalDateTime;

public record AuditLogEvent(
    String eventType,
    String entityType,
    Integer entityId,
    String action,
    String username,
    LocalDateTime timestamp,
    String details
) {}