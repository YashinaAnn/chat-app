package ru.yasha.webchat.service;

import org.springframework.data.domain.Pageable;
import ru.yasha.webchat.entity.ChatMessage;

import java.util.List;

public interface ChatService {

    void processMessage(ChatMessage message);

    List<ChatMessage> getMessages(Pageable pageable);
}
