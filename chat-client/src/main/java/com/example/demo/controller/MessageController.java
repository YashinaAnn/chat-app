package com.example.demo.controller;

import com.example.demo.dto.ChatMessageDto;
import com.example.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final ChatService chatService;

    @ModelAttribute("messages")
    public List<ChatMessageDto> populateMessages() {
        System.out.println("Updating messages....");
        return chatService.getMessages();
    }

    @GetMapping({"/", "/messages"})
    public String getMessages(Model model) {
        return "messages";
    }

    @GetMapping("/leaveChat")
    public String leaveChat(Model model, @AuthenticationPrincipal OidcUser user) {
        chatService.leaveChat(user);
        return "index";
    }

    @PostMapping("/messages")
    public String sendMessage(ChatMessageDto message, @AuthenticationPrincipal OidcUser user) {
        chatService.sendMessage(message, user);
        return "redirect:messages";
    }
}
