package com.example.demo.client;

import com.example.demo.config.AppConfigs;
import com.example.demo.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageClient {

    private final WebClient webClient;
    private final AppConfigs configs;

    public List<ChatMessage> getMessages() {
        log.info("Retrieving messages...");
        List<ChatMessage> messages = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/messages")
                        .queryParam("page", configs.getPage())
                        .queryParam("size", configs.getSize())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ChatMessage>>(){})
                .block();
        log.info("Messages retrieved: {}", messages);
        return messages;
    }
}
