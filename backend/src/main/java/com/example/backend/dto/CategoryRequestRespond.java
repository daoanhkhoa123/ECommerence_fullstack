package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestRespond(
    @NotBlank(message = "Category name is required")
    String name,
    String description,

    // for respond only
    Integer id
) {}
