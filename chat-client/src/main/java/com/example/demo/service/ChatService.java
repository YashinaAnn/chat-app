package com.example.demo.service;

import com.example.demo.model.ChatMessage;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;

public interface ChatService {

    void connect() throws Exception;
    void sendMessage(ChatMessage message, OidcUser user);
    void disconnect();
    List<ChatMessage> getMessages();
}
