package ru.yasha.chat.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.input")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppConfigs {

    private String userJoinedTopic;
    private String userLeftTopic;
    private String messagesTopic;
}
