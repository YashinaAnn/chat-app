package com.example.demo.service.impl;

import com.example.demo.client.MessageClient;
import com.example.demo.config.AppConfigs;
import com.example.demo.dto.ChatMessageDto;
import com.example.demo.dto.UserDto;
import com.example.demo.service.ChatService;
import com.example.demo.websocket.MyStompSessionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final WebSocketStompClient stompClient;
    private final MyStompSessionHandler sessionHandler;
    private final MessageClient messageClient;
    private final AppConfigs configs;

    private ConcurrentHashMap<String, StompSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void joinChat(Authentication authentication) throws Exception {
        OidcUser user = (OidcUser) authentication.getPrincipal();
        UserDto userDto = UserDto.of(user);
        log.info("Joining chat: {}", userDto);
        StompSession stompSession = stompClient.connect(configs.getWsUrl(), sessionHandler).get();
        log.info("Connected to the chat server: %s".formatted(configs.getWsUrl()));
        sessions.put(userDto.getEmail(), stompSession);
        synchronized (stompSession) {
            stompSession.send(configs.getUserJoinDestination(), userDto);
        }
    }

    @Override
    public void leaveChat(OidcUser user) {
        UserDto userDto = UserDto.of(user);
        log.info("Leaving chat: {}", userDto);

        StompSession session = sessions.get(userDto.getEmail());
        synchronized (session) {
            session.send(configs.getUserLeftDestination(), userDto);
            session.disconnect();
        }
    }

    @Override
    public void sendMessage(ChatMessageDto message, OidcUser user) {
        // TODO - to add null 'stompSession' check
        //if (null == stompSession.get()) return;

        message.setUserName(user.getName());
        message.setUserEmail(user.getEmail());
        log.info("Sending message '%s' to '%s'".formatted(message, configs.getChatDestination()));
        StompSession session = sessions.get(user.getEmail());
        synchronized (session) {
            session.send(configs.getChatDestination(), message);
        }
    }

    @Override
    public List<ChatMessageDto> getMessages() {
        return messageClient.getMessages();
    }
}
