package com.example.demo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.input")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppConfigs {

    private String wsUrl;
    private String serverUrl;
    private String messagesSubscription;
    private String userJoinSubscription;
    private String userLeftSubscription;
    private String chatDestination;
    private String userJoinDestination;
    private String userLeftDestination;
    private String logoutRedirectUrl;
    private int page;
    private int size;
}
