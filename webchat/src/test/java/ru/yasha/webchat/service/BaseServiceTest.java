package ru.yasha.webchat.service;

import ru.yasha.webchat.dto.UserDto;
import ru.yasha.webchat.entity.User;

import java.util.UUID;

public class BaseServiceTest {

    protected UserDto getUserDto() {
        return UserDto.builder()
                .name("user1")
                .email("user1@test.com")
                .build();
    }

    private User getUser(boolean active, String name, String email) {
        return User.builder()
                .name(name)
                .email(email)
                .active(active)
                .build();
    }

    protected User getUser(boolean active) {
        return getUser(active, "test1", "test1@test.com");
    }

    protected User getRandomUser(boolean active) {
        UUID uuid = UUID.randomUUID();
        return getUser(active, "test_%s".formatted(uuid), "test_%s@test.com".formatted(uuid));
    }
}
