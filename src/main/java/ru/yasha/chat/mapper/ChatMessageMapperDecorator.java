package ru.yasha.chat.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yasha.chat.dto.ChatMessageDto;
import ru.yasha.chat.entity.ChatMessage;
import ru.yasha.chat.entity.User;
import ru.yasha.chat.exception.UserNotFoundException;
import ru.yasha.chat.repository.UserRepository;

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
