package ru.yasha.webchat.service;

import ru.yasha.webchat.dto.UserDto;

import java.util.List;

public interface UserService {

    void join(UserDto user);
    void left(UserDto user);

    List<UserDto> getActiveUsers();
}
