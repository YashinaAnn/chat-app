package com.example.demo.security;

import com.example.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MyAuthenticationSuccessListener {

    private final ChatService chatService;

    @EventListener(AuthenticationSuccessEvent.class)
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        try {
            chatService.joinChat(event.getAuthentication());
        } catch (Exception e) {
            log.error("Unable to connect to the chat server: ", e);
        }
    }
}