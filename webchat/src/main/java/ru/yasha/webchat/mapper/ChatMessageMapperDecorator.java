package ru.yasha.webchat.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yasha.webchat.dto.ChatMessageDto;
import ru.yasha.webchat.entity.ChatMessage;
import ru.yasha.webchat.entity.User;
import ru.yasha.webchat.exception.UserNotFoundException;
import ru.yasha.webchat.repository.UserRepository;

public abstract class ChatMessageMapperDecorator implements ChatMessageMapper {

    @Autowired
    private ChatMessageMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ChatMessage dtoToMessage(ChatMessageDto dto) {
        ChatMessage message = mapper.dtoToMessage(dto);
        User user = userRepository.findByName(dto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Email is not found: " + dto.getUsername()));
        message.setUser(user);
        return message;
    }
}
