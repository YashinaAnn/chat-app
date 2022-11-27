package ru.yasha.webchat.service;

import org.springframework.data.domain.Pageable;
import ru.yasha.webchat.dto.ChatMessageDto;

import java.util.List;

public interface MessageService {

    void processMessage(ChatMessageDto message);

    List<ChatMessageDto> getMessages(Pageable pageable);
}
