package com.example.backend.dto;

import java.time.LocalDateTime;

import com.example.backend.enums.ChatRole;

public record ChatMessageRespond(
    String message,
    ChatRole role,
    LocalDateTime created_at
) {}
