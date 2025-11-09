package com.example.backend.kafka.cache;

import com.example.backend.dto.ChatMessageDTO;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatReplyCache {
    private final ConcurrentHashMap<String, ChatMessageDTO> replies = new ConcurrentHashMap<>();

    public void put(String correlationId, ChatMessageDTO reply) {
        replies.put(correlationId, reply);
    }

    public ChatMessageDTO get(String correlationId) {
        return replies.get(correlationId);
    }

    public void remove(String correlationId) {
        replies.remove(correlationId);
    }
}
