package ru.yasha.webchat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.yasha.webchat.dto.UserDto;
import ru.yasha.webchat.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @MessageMapping("/users/join")
    public void joinUser(UserDto user) {
        userService.join(user);
    }

    @MessageMapping("/users/left")
    public void leftUser(UserDto user) {
        userService.left(user);
    }
}
