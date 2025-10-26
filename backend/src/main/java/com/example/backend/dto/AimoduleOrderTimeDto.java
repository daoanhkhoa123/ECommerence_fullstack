package com.example.backend.dto;

import java.time.LocalDateTime;

public record AimoduleOrderTimeDto(
        Integer orderId,
        LocalDateTime placedAt
) {}
