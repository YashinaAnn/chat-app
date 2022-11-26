package ru.yasha.webchat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/users/add")
    @ResponseBody
    public ResponseEntity<UserDto> addUser(@RequestParam("name") String name,
                                           @AuthenticationPrincipal OidcUser oidcUser) {
        UserDto user = UserDto.builder()
                .name(name)
                .email(oidcUser.getEmail())
                .build();
        userService.join(user);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users")
    @ResponseBody
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto user,
                                           @AuthenticationPrincipal OidcUser oidcUser) {
        user.setEmail(oidcUser.getEmail());
        userService.join(user);
        return ResponseEntity.ok().body(user);
    }
}
