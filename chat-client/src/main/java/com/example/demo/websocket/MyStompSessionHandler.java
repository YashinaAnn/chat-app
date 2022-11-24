package com.example.demo.websocket;

import com.example.demo.config.AppConfigs;
import com.example.demo.dto.ChatMessageDto;
import com.example.demo.dto.UserDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Slf4j
@Component
public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    @Autowired
    private AppConfigs configs;

    @Getter
    private StompSession session;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        this.session = session;
        synchronized (session) {
            log.info("New session established: " + session.getSessionId());
            session.subscribe(configs.getMessagesSubscription(), new MessageHandler());
            log.info("Subscribed to: %s".formatted(configs.getMessagesSubscription()));
            session.subscribe(configs.getUserJoinSubscription(), new UserJoinHandler());
            log.info("Subscribed to: %s".formatted(configs.getUserJoinSubscription()));
            session.subscribe(configs.getUserLeftSubscription(), new UserLeftHandler());
            log.info("Subscribed to: %s".formatted(configs.getUserLeftSubscription()));
        }
    }

    @Override
    public void handleException(StompSession session, StompCommand command,
                                StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Exception occurred:" , exception);
    }

    public static class MessageHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return ChatMessageDto.class;
        }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        ChatMessageDto msg = (ChatMessageDto) payload;
        log.info("Received message: %s".formatted(msg.getText()));
    }
}

    public static class UserJoinHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return UserDto.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            UserDto msg = (UserDto) payload;
            log.info("New user joined: %s".formatted(msg.getEmail()));
        }
    }

    public static class UserLeftHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return UserDto.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            UserDto msg = (UserDto) payload;
            log.info("User left: %s".formatted(msg.getEmail()));
        }
    }
}
