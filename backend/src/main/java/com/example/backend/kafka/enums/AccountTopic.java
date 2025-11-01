package com.example.backend.kafka.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountTopic {
    CUSTOMER("account.customer"),
    VENDOR("account.vendor");

    private final String name;
}
