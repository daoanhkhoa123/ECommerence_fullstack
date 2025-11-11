package com.example.backend.kafka.consumer;

import com.example.backend.application.ChatMessageApplication;
import com.example.backend.kafka.dto.ChatMessageEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class ChatReplyConsumer {

    private final ChatMessageApplication chatMessageApplication;
    private static final Logger log = LoggerFactory.getLogger(ChatReplyConsumer.class);

@KafkaListener(topics = "chat.message.system.v1")
    public void consumeSystemChatMessage(ChatMessageEvent event) {
        try {
            chatMessageApplication.recieveSystemMesageFromEvent(event);

            // Professional logging
            log.info(
                "Consumed system chat message for accountId={} | role={} | message='{}' | timestamp={}",
                event.accountId(), event.role(), event.message(), event.created_at()
            );

        } catch (Exception ex) {
            log.error(
                "Failed to process system chat message for accountId={} | role={} | message='{}'",
                event.accountId(), event.role(), event.message(), ex);
        }
    }
}
