package com.example.backend.kafka.enums; 

public enum AuditTopic {

    USER_ACTIVITY("audit.user.activity"),
    TRANSACTION_LOG("audit.transaction.log"),
    SYSTEM_EVENT("audit.system.event");

    private final String name;

    AuditTopic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
