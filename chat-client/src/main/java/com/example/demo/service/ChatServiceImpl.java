package com.example.demo.service;

import com.example.demo.client.MessageClient;
import com.example.demo.config.AppConfigs;
import com.example.demo.model.ChatMessage;
import com.example.demo.websocket.MyStompSessionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final WebSocketStompClient stompClient;
    private final MyStompSessionHandler sessionHandler;
    private final MessageClient messageClient;
    private final AppConfigs configs;

    private StompSession stompSession;

    public void connect() throws Exception {
        log.info("Connecting to chat server: %s".formatted(configs.getWsUrl()));
        stompSession = stompClient.connect(configs.getWsUrl(), sessionHandler).get();
    }

    public void sendMessage(ChatMessage message, OidcUser user) {
        // TODO - to add null 'stompSession' check
        if (null == stompSession) return;

        message.setUsername(user.getName());
        log.info("Sending message '%s' to '%s'".formatted(message, configs.getPublishDestination()));
        stompSession.send(configs.getPublishDestination(), message);
    }

    public void disconnect() {
        // TODO - to add null 'stompSession' check
        if (null == stompSession) return;

        log.info("Disconnecting from chat server");
        stompSession.disconnect();
        stompSession = null;
    }

    @Override
    public List<ChatMessage> getMessages() {
        return messageClient.getMessages();
    }
}
