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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final ChatService chatService;

    @GetMapping({"/", "/messages"})
    public String getMessages(Model model, @RequestParam Optional<Boolean> fetch) {
        if (fetch.isPresent()) {
            System.out.println("Fetching messages....");
            model.addAttribute("messages", chatService.getMessages());
        }
        return "messages";
    }

    @GetMapping("/leaveChat")
    public String leaveChat(Model model, @AuthenticationPrincipal OidcUser user) {
        chatService.leaveChat(user);
        return "redirect:logout";
    }

    @PostMapping("/messages")
    public String sendMessage(ChatMessageDto message, @AuthenticationPrincipal OidcUser user) {
        chatService.sendMessage(message, user);
        return "redirect:messages";
    }

    @GetMapping("/messages/receive")
    public String receiveMessage(Model model, ChatMessageDto messageDto) {
        List<ChatMessageDto> messages = new ArrayList<>();
        messages.addAll((List<ChatMessageDto>) model.getAttribute("messages"));
        messages.add(messageDto);
        return "messages";
    }
}
