package com.example.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VendorRequest(
    @Valid AccountRegisterRequest accountRequest,
    @NotBlank String shopName,
    String description,
    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be 10-15 digits") String phone
) {}
