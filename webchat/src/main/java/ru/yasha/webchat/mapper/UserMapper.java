package ru.yasha.webchat.mapper;

import ru.yasha.webchat.dto.UserDto;
import ru.yasha.webchat.entity.User;

public interface UserMapper {

    User dtoToUser(UserDto dto);
    UserDto userToDto(User user);
}
