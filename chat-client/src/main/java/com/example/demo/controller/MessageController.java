package com.example.demo.controller;

import com.example.demo.dto.ChatMessageDto;
import com.example.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final ChatService chatService;

    @GetMapping("/messages")
    public String getMessages(Model model) {
        List<ChatMessageDto> messages = chatService.getMessages();
        model.addAttribute("messages", messages);
        return "messages";
    }

    @PostMapping
    public void sendMessage(ChatMessageDto message, @AuthenticationPrincipal OidcUser user) {
        chatService.sendMessage(message, user);
    }
}
