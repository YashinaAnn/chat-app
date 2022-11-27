package ru.yasha.webchat.service;

import ru.yasha.webchat.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto join(UserDto user);
    void leave(UserDto user);

    List<UserDto> getActiveUsers();
}
