package com.example.backend.kafka.dto;

import java.time.LocalDate;

public record CustomerCreateUpdateEvent(
    Integer actorId,    

    Integer customerId,
    String email,
    String fullName,
    String phone,
    String adress,
    LocalDate birDate
) {}

