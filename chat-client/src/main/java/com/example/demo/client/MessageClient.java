package com.example.demo.client;

import com.example.demo.config.AppConfigs;
import com.example.demo.dto.ChatMessageDto;
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

    public List<ChatMessageDto> getMessages() {
        log.info("Retrieving messages...");
        List<ChatMessageDto> messages = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/messages")
                        .queryParam("page", configs.getPage())
                        .queryParam("size", configs.getSize())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ChatMessageDto>>(){})
                .block();
        log.info("Messages retrieved: {}", messages);
        return messages;
    }
}
