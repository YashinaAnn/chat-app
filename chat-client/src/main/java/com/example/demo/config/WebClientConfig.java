package com.example.demo.config;

import com.example.demo.config.AppConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Autowired
    private AppConfigs configs;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(configs.getServerUrl())
                .build();
    }
}
