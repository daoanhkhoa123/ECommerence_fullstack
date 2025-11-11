package com.example.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.ChatMessageRequest;
import com.example.backend.entity.Account;
import com.example.backend.entity.ChatMessage;
import com.example.backend.enums.ChatRole;
import com.example.backend.kafka.dto.ChatMessageEvent;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final AccountRepository accountRepository;

    private ChatMessage buildEntity(ChatMessageRequest body, ChatRole role)
    {
        ChatMessage chatMessage =  new ChatMessage();
        chatMessage.setContent(body.message());

        chatMessage.setRole(role);
        return chatMessage;
    }

    private ChatMessage buildEntitiy(ChatMessageEvent event)
    {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(event.message());
        
        Integer userId = event.accountId();
        Account account = accountRepository.findById(userId).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Account not found with id: " + userId));
        
        chatMessage.setAccount(account);
        chatMessage.setCreatedAt(event.created_at());
        chatMessage.setRole(ChatRole.SYSTEM);

        return chatMessage;
    }

    public List<ChatMessage> findAllByAccountId(Integer accountId)
    {
        if (!accountRepository.existsById(accountId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Account not found with id: " + accountId
            );
        }

        return chatMessageRepository.findByAccountId(accountId);
    }

    public ChatMessage saveUserMessage(ChatMessageRequest request, Integer accountId)
    {
        ChatMessage chatMessage = buildEntity(request, ChatRole.USER);

        Account account =  accountRepository.findById(accountId).orElseThrow();
        chatMessage.setAccount(account);
        
        return chatMessageRepository.save(chatMessage);
    }

    public ChatMessage saveSystemMessage(ChatMessageEvent event)
       {
        ChatMessage chatMessage = buildEntitiy(event);
        
        return chatMessageRepository.save(chatMessage);
    }
}
