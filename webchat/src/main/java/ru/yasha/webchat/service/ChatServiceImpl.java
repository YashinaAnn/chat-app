package ru.yasha.webchat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.yasha.webchat.entity.ChatMessage;
import ru.yasha.webchat.repository.ChatMessageRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void processMessage(ChatMessage message) {
        log.info("Processing message: %s".formatted(message));
        message = repository.save(message);
        messagingTemplate.convertAndSend("/topic/messages", message);
    }

    @Override
    public List<ChatMessage> getMessages(Pageable pageable) {
        return repository.findLast(pageable);
    }
}
