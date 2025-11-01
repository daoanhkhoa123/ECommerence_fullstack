package com.example.backend.kafka.dto;

import java.time.LocalDateTime;

public record AuditEvent(
    String serviceName,     // e.g. "order-service" — which service emitted this event
    
    String actorId,         // user ID or system ID who triggered the action
    String actorRole,       // e.g. "CUSTOMER", "ADMIN", "SYSTEM"
    String action,          // e.g. "ORDER_CREATED", "PAYMENT_SUCCESS", "PRODUCT_UPDATED"
    
    String entityType,      // e.g. "Order", "Payment", "Product"
    String entityId,        // e.g. "12345" — ID of the affected entity
    String entityBody,  // (optional) JSON or stringified summary of entity at that time
    
    String result,          // e.g. "SUCCESS", "FAILED", "CANCELLED"
    String message,         // human-readable context or error reason
    LocalDateTime timestamp // when it happened
) {}
