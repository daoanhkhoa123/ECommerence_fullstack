package com.example.backend.kafka.consumer;

import com.example.backend.dto.ChatMessageDTO;
import com.example.backend.kafka.cache.ChatReplyCache;
import com.example.backend.kafka.dto.ChatMessageEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class ChatReplyConsumer {

    private static final Logger log = LoggerFactory.getLogger(ChatReplyConsumer.class);

    private final ChatReplyCache replyCache;

    @KafkaListener(topics = "chat.message.v1", groupId = "chat-service-group")
    public void consumeReply(ChatMessageEvent event) {
        ChatMessageDTO dto = new ChatMessageDTO(event.message());

        // Correlation ID can be accountId or a dedicated field if you add one
        String correlationId = event.accountId().toString();

        log.info("Consumed reply for correlationId={}, role={}, message={}",
                 correlationId, event.role(), event.message());

        replyCache.put(correlationId, dto);
    }
}
