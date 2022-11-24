package ru.yasha.webchat.mapper;

import org.mapstruct.Mapper;
import ru.yasha.webchat.dto.UserDto;
import ru.yasha.webchat.entity.User;

@Mapper
public interface UserMapper {

    User dtoToUser(UserDto dto);
    UserDto userToDto(User user);
}
