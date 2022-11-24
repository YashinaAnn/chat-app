package ru.yasha.webchat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.yasha.webchat.dto.UserDto;
import ru.yasha.webchat.service.UserService;

import java.util.List;

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

    @GetMapping("/users/active")
    @ResponseBody
    public ResponseEntity<List<UserDto>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }
}
