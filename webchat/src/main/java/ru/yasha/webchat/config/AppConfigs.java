package ru.yasha.webchat.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.input")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppConfigs {

    private int page;
    private int size;
}
