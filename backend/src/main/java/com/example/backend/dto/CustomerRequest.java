package com.example.backend.dto;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRequest(
    @Valid AccountRegisterRequest accountRequest,
    @NotBlank @Size(max = 64) String fullName,
    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be 10-15 digits") String phone,
    @NotBlank String address,
    @Past(message = "Birthdate must be in the past") LocalDate birthDate
) {}
