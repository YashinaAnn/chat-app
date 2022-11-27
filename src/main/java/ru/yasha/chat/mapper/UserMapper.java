package ru.yasha.chat.mapper;

import org.mapstruct.Mapper;
import ru.yasha.chat.dto.UserDto;
import ru.yasha.chat.entity.User;

@Mapper
public interface UserMapper {

    User dtoToUser(UserDto dto);
    UserDto userToDto(User user);
}
