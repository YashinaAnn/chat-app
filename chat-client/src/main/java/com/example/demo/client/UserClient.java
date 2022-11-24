package com.example.demo.client;

import com.example.demo.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserClient {

    private final WebClient webClient;

    public List<UserDto> getActiveUsers() {
        log.info("Retrieving active users...");
        List<UserDto> users = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/active")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserDto>>(){})
                .block();
        log.info("Users retrieved: {}", users);
        return users;
    }
}
