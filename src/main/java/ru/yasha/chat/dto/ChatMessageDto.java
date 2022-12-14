package ru.yasha.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto implements Serializable {

    private Long id;
    private String text;
    private String time;
    private String username;
}
