package com.example.backend.dto;

import java.time.LocalDate;

public record CustomerRespond(
    Integer customerId,
    String email,
    String fullName,
    String phone,
    String adress,
    LocalDate birDate
) {}
