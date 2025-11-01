package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AccountUpdateRequest(
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,
    
    String password
) {}