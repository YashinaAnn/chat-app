package com.example.demo.service;

import com.example.demo.dto.ChatMessageDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;

public interface ChatService {

    void joinChat(Authentication authentication) throws Exception;
    void leaveChat(OidcUser user);
    void sendMessage(ChatMessageDto message, OidcUser user);
    List<ChatMessageDto> getMessages();
}
