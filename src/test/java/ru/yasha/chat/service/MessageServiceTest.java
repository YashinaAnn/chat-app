package ru.yasha.chat.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.yasha.chat.dto.ChatMessageDto;
import ru.yasha.chat.entity.ChatMessage;
import ru.yasha.chat.entity.User;
import ru.yasha.chat.mapper.ChatMessageMapper;
import ru.yasha.chat.repository.ChatMessageRepository;
import ru.yasha.chat.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class MessageServiceTest extends BaseServiceTest {

    @Autowired
    private MessageService messageService;
    @Autowired
    private ChatMessageRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatMessageMapper messageMapper;

    @MockBean
    private SimpMessagingTemplate template;

    @Test
    void testGetLastNMessages_EmptyList() {
        List<ChatMessageDto> messages = messageService.getMessages(PageRequest.of(0, 10));
        assertThat(messages).asList().isEmpty();
    }

    @Test
    void testGetLastNMessages_All() {
        User user1 = userRepository.save(getUser(true));
        repository.save(ChatMessage.builder().user(user1).text("hi").build());
        repository.save(ChatMessage.builder().user(user1).text("hello").build());
        repository.save(ChatMessage.builder().user(user1).text("how are you?").build());

        List<ChatMessageDto> messages = messageService.getMessages(PageRequest.of(0, 2));
        assertThat(messages).hasSize(2);
        assertThat(messages).isSortedAccordingTo(Comparator.comparing(ChatMessageDto::getTime).reversed());
    }

    @Test
    void testGetLastNMessages_Partial() {
        User user1 = userRepository.save(getUser(true));
        repository.save(ChatMessage.builder().user(user1).text("hi").build());

        List<ChatMessageDto> messages = messageService.getMessages(PageRequest.of(0, 2));
        assertThat(messages).hasSize(1);
    }

    @Test
    void testProcessMessage() {
        User user = userRepository.save(getUser(true));
        ChatMessageDto messageDto = ChatMessageDto.builder()
                .text("test message: %s".formatted(UUID.randomUUID()))
                .username(user.getName())
                .build();
        doNothing().when(template).convertAndSend("/topic/messages", messageDto);
        assertThat(repository.findByText(messageDto.getText())).isEmpty();

        messageService.processMessage(messageDto);

        ChatMessage message = repository.findByText(messageDto.getText()).orElse(null);
        assertThat(message).isNotNull();
        assertThat(message.getUser()).isEqualTo(user);
        verify(template, times(1))
                .convertAndSend("/topic/messages", messageMapper.messageToDto(message));
    }
}