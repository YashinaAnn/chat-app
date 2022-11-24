package com.example.demo.websocket;

import com.example.demo.config.AppConfigs;
import com.example.demo.dto.ChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Slf4j
@Component
public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    @Autowired
    private AppConfigs configs;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("New session established: " + session.getSessionId());
        session.subscribe(configs.getSubscribeDestination(), this);
        log.info("Subscribed to: %s".formatted(configs.getSubscribeDestination()));
    }

    @Override
    public void handleException(StompSession session, StompCommand command,
                                StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Exception occurred:" , exception);
    }

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
