package com.example.backend.application;

import com.example.backend.dto.ChatMessageDTO;
import com.example.backend.kafka.cache.ChatReplyCache;
import com.example.backend.kafka.dto.ChatMessageEvent;
import com.example.backend.kafka.enums.ChatRole;
import com.example.backend.kafka.producer.ChatProducer;
import com.example.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatApplication {

    private final ChatProducer chatProducer;
    private final ChatReplyCache replyCache;
    private final JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(ChatApplication.class);

    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        Integer accountId = jwtService.getCurrentUserId();
        ChatMessageEvent event = new ChatMessageEvent(
                accountId,
                message.message(),
                ChatRole.USER,
                LocalDateTime.now()
        );

        String correlationId = chatProducer.produce(event);
        log.info("Produced chat message with correlationId={}, message={}", correlationId, message.message());

        // Wait for reply (up to 5 seconds)
        ChatMessageDTO reply = null;
        long timeoutMs = 5000;
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < timeoutMs) {
            reply = replyCache.get(correlationId);
            if (reply != null) break;
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        }

        if (reply == null) {
            log.warn("No reply received for correlationId={} within timeout", correlationId);
            return new ChatMessageDTO("No reply received");
        }

        replyCache.remove(correlationId);
        return reply;
    }
}
