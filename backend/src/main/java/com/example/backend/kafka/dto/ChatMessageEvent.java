package com.example.backend.kafka.dto;

import java.time.LocalDateTime;

import com.example.backend.enums.ChatRole;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatMessageEvent(

    @JsonProperty("accountId") // matches Python alias
    Integer accountId,

    String message,

    ChatRole role,

    @JsonAlias({"timestamp", "created_at"}) // accept both names
    LocalDateTime created_at

) {}
