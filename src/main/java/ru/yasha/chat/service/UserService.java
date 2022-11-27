package ru.yasha.chat.service;

import ru.yasha.chat.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto join(UserDto user);
    void leave(UserDto user);

    List<UserDto> getActiveUsers();
}
