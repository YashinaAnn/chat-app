package ru.yasha.webchat.service;

import ru.yasha.webchat.dto.UserDto;
import ru.yasha.webchat.entity.User;

public class BaseServiceTest {

    protected UserDto getUserDto() {
        return UserDto.builder()
                .email("user1@test.com")
                .name("user1")
                .build();
    }

    protected User getUser(boolean active) {
        return User.builder()
                .email("test1@test.com")
                .name("test1")
                .active(active)
                .build();
    }
}
