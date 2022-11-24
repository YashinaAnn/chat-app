package com.example.demo.service;

import com.example.demo.client.MessageClient;
import com.example.demo.config.AppConfigs;
import com.example.demo.dto.ChatMessageDto;
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

    private ThreadLocal<StompSession> stompSession = new ThreadLocal<>();

    public void connect() throws Exception {
        log.info("Connecting to chat server: %s".formatted(configs.getWsUrl()));
        stompSession.set(stompClient.connect(configs.getWsUrl(), sessionHandler).get());
    }

    public void sendMessage(ChatMessageDto message, OidcUser user) {
        // TODO - to add null 'stompSession' check
        if (null == stompSession.get()) return;

        message.setUserName(user.getName());
        message.setUserEmail(user.getEmail());
        log.info("Sending message '%s' to '%s'".formatted(message, configs.getPublishDestination()));
        stompSession.get().send(configs.getPublishDestination(), message);
    }

    public void disconnect() {
        // TODO - to add null 'stompSession' check
        if (null == stompSession.get()) return;

        log.info("Disconnecting from chat server");
        stompSession.get().disconnect();
        stompSession = null;
    }

    @Override
    public List<ChatMessageDto> getMessages() {
        return messageClient.getMessages();
    }
}
