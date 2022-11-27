package ru.yasha.webchat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.yasha.webchat.dto.UserDto;
import ru.yasha.webchat.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<List<UserDto>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @PostMapping("/users")
    @ResponseBody
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto user,
                                           @AuthenticationPrincipal OidcUser oidcUser) {
        user.setEmail(oidcUser.getEmail());
        return ResponseEntity.ok().body(userService.join(user));
    }

    @MessageMapping("/users/left")
    public void leftUser(UserDto user) {
        userService.left(user);
    }
}
