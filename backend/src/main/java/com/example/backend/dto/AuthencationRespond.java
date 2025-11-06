package com.example.backend.dto;

public record AuthencationRespond(
    String token,
    String type,
    long expiresIn,
    String username,
    String role
) {}
