package ru.yasha.webchat.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.yasha.webchat.dto.ChatMessageDto;
import ru.yasha.webchat.entity.ChatMessage;
import ru.yasha.webchat.mapper.ChatMessageMapper;
import ru.yasha.webchat.repository.ChatMessageRepository;
import ru.yasha.webchat.service.MessageService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final ChatMessageRepository repository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageMapper messageMapper;

    @Override
    public void processMessage(ChatMessageDto messageDto) {
        log.debug("Processing message: {}", messageDto);
        ChatMessage message = messageMapper.dtoToMessage(messageDto);
        repository.save(message);
        messagingTemplate.convertAndSend("/topic/messages", messageMapper.messageToDto(message));
    }

    @Override
    public List<ChatMessageDto> getMessages(Pageable pageable) {
        return repository.findLast(pageable)
                .stream().map(messageMapper::messageToDto)
                .toList();
    }
}
