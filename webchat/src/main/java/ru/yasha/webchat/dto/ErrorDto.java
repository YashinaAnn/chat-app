package ru.yasha.webchat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ErrorDto {

    private String message;

    public static ErrorDto of(String message) {
        return ErrorDto.builder()
                .message(message)
                .build();
    }

}
