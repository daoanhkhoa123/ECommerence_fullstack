package com.example.backend.kafka.dto;

import java.time.LocalDate;

import com.example.backend.kafka.enums.CRUDType;

public record CustomerEvent(
    CRUDType evenType,
    Integer customerId,

    String email,
    String fullName,
    String phone,
    String adress,
    LocalDate birDate
) {}

