package ru.yasha.webchat.service;

import org.springframework.data.domain.Pageable;
import ru.yasha.webchat.dto.ChatMessageDto;
import ru.yasha.webchat.entity.ChatMessage;

import java.util.List;

public interface ChatService {

    void processMessage(ChatMessageDto message);

    List<ChatMessageDto> getMessages(Pageable pageable);
}
