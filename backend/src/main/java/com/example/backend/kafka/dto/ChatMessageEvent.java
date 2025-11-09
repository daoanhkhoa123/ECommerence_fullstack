package com.example.backend.kafka.dto;

import java.time.LocalDateTime;

import com.example.backend.kafka.enums.ChatRole;

public record ChatMessageEvent(
    Integer accountId,
    String message,
    ChatRole role,
    LocalDateTime timestamp
) {}
