package ru.yasha.webchat.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.yasha.webchat.entity.ChatMessage;
import ru.yasha.webchat.repository.ChatMessageRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class ChatServiceTest {

    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatMessageRepository repository;

    @MockBean
    private SimpMessagingTemplate template;

    @Test
    void testGetLastNMessages_EmptyList() {
        List<ChatMessage> messages = chatService.getMessages(PageRequest.of(0, 10));
        assertThat(messages).asList().isEmpty();
    }

    @Test
    void testGetLastNMessages_All() {
        repository.save(ChatMessage.builder().text("hi").build());
        repository.save(ChatMessage.builder().text("hello").build());
        repository.save(ChatMessage.builder().text("how are you?").build());

        List<ChatMessage> messages = chatService.getMessages(PageRequest.of(0, 2));
        assertThat(messages).hasSize(2);
        assertThat(messages).isSortedAccordingTo(Comparator.comparing(ChatMessage::getTime).reversed());
    }

    @Test
    void testGetLastNMessages_Partial() {
        repository.save(ChatMessage.builder().text("hi").build());

        List<ChatMessage> messages = chatService.getMessages(PageRequest.of(0, 2));
        assertThat(messages).hasSize(1);
    }

    @Test
    void testProcessMessage() {
        ChatMessage message = ChatMessage.builder()
                .text("test message: %s".formatted(UUID.randomUUID()))
                .build();
        doNothing().when(template).convertAndSend("/topic/messages", message);
        assertThat(repository.findByText(message.getText())).isEmpty();

        chatService.processMessage(message);

        assertThat(repository.findByText(message.getText())).isNotEmpty();
    }
}