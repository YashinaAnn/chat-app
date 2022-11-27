package ru.yasha.webchat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.yasha.webchat.dto.ChatMessageDto;
import ru.yasha.webchat.service.MessageService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/chat")
    public void processMessage(ChatMessageDto message) {
        messageService.processMessage(message);
    }

    @GetMapping("/messages")
    @ResponseBody
    public ResponseEntity<List<ChatMessageDto>> getMessages(
            @RequestParam(required = false, defaultValue = "${app.input.page}") int page,
            @RequestParam(required = false, defaultValue = "${app.input.size}") int size) {
        return ResponseEntity.ok(messageService.getMessages(PageRequest.of(page, size)));
    }
}
