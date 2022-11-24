package ru.yasha.webchat.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.yasha.webchat.dto.ChatMessageDto;
import ru.yasha.webchat.entity.ChatMessage;
import ru.yasha.webchat.entity.User;
import ru.yasha.webchat.repository.ChatMessageRepository;
import ru.yasha.webchat.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class ChatServiceTest extends BaseServiceTest {

    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatMessageRepository repository;
    @Autowired
    private UserRepository userRepository;

    @MockBean
    private SimpMessagingTemplate template;

    @Test
    void testGetLastNMessages_EmptyList() {
        List<ChatMessageDto> messages = chatService.getMessages(PageRequest.of(0, 10));
        assertThat(messages).asList().isEmpty();
    }

    @Test
    void testGetLastNMessages_All() {
        User user1 = userRepository.save(getUser(true));
        repository.save(ChatMessage.builder().user(user1).text("hi").build());
        repository.save(ChatMessage.builder().user(user1).text("hello").build());
        repository.save(ChatMessage.builder().user(user1).text("how are you?").build());

        List<ChatMessageDto> messages = chatService.getMessages(PageRequest.of(0, 2));
        assertThat(messages).hasSize(2);
        assertThat(messages).isSortedAccordingTo(Comparator.comparing(ChatMessageDto::getTime).reversed());
    }

    @Test
    void testGetLastNMessages_Partial() {
        User user1 = userRepository.save(getUser(true));
        repository.save(ChatMessage.builder().user(user1).text("hi").build());

        List<ChatMessageDto> messages = chatService.getMessages(PageRequest.of(0, 2));
        assertThat(messages).hasSize(1);
    }

    @Test
    void testProcessMessage() {
        User user = userRepository.save(getUser(true));
        ChatMessageDto messageDto = ChatMessageDto.builder()
                .text("test message: %s".formatted(UUID.randomUUID()))
                .userName(user.getName())
                .userEmail(user.getEmail())
                .build();
        doNothing().when(template).convertAndSend("/topic/messages", messageDto);
        assertThat(repository.findByText(messageDto.getText())).isEmpty();

        chatService.processMessage(messageDto);

        ChatMessage message = repository.findByText(messageDto.getText()).orElse(null);
        assertThat(message).isNotNull();
        assertThat(message.getUser()).isEqualTo(user);

        messageDto.setId(message.getId());
        messageDto.setTime(message.getTime().toString());
        verify(template, times(1))
                .convertAndSend("/topic/messages", messageDto);
    }
}