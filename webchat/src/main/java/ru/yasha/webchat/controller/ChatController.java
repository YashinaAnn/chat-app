package ru.yasha.webchat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.yasha.webchat.entity.ChatMessage;
import ru.yasha.webchat.service.ChatService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat")
    public void processMessage(ChatMessage message) {
        chatService.processMessage(message);
    }

    @GetMapping("/messages")
    @ResponseBody
    public ResponseEntity<List<ChatMessage>> getMessages(
            @RequestParam(required = false, defaultValue = "${app.input.page}") int page,
            @RequestParam(required = false, defaultValue = "${app.input.size}") int size) {
        return ResponseEntity.ok(chatService.getMessages(PageRequest.of(page, size)));
    }
}
