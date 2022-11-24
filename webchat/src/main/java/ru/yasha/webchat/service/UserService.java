package ru.yasha.webchat.service;

import ru.yasha.webchat.dto.UserDto;

public interface UserService {

    void join(UserDto user);
    void left(UserDto user);
}
