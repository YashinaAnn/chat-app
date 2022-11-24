package ru.yasha.webchat.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yasha.webchat.dto.ChatMessageDto;
import ru.yasha.webchat.entity.ChatMessage;
import ru.yasha.webchat.entity.User;
import ru.yasha.webchat.exception.UserNotFoundException;
import ru.yasha.webchat.repository.UserRepository;

@Mapper
@DecoratedWith(ChatMessageMapperDecorator.class)
public interface ChatMessageMapper {

    ChatMessage dtoToMessage(ChatMessageDto dto);

    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.email", target = "userEmail")
    ChatMessageDto messageToDto(ChatMessage message);
}
