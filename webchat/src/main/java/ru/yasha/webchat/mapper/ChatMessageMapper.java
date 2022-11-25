package ru.yasha.webchat.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yasha.webchat.dto.ChatMessageDto;
import ru.yasha.webchat.entity.ChatMessage;

@Mapper
@DecoratedWith(ChatMessageMapperDecorator.class)
public interface ChatMessageMapper {

    ChatMessage dtoToMessage(ChatMessageDto dto);

    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "time", target = "time", dateFormat = "EEE, MMM d, yy")
    ChatMessageDto messageToDto(ChatMessage message);
}
