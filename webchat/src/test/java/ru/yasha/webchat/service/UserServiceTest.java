package ru.yasha.webchat.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.yasha.webchat.dto.UserDto;
import ru.yasha.webchat.entity.User;
import ru.yasha.webchat.mapper.UserMapper;
import ru.yasha.webchat.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class UserServiceTest extends BaseServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @MockBean
    private SimpMessagingTemplate template;

    @Test
    void testJoinNewUser() {
        UserDto userDto = getUserDto();
        doNothing().when(template).convertAndSend("/topic/users/join", userDto);

        userService.join(userDto);

        User user = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.isActive()).isTrue();

        verify(template, times(1))
                .convertAndSend("/topic/users/join", userMapper.userToDto(user));
    }

    @Test
    void testJoinExistingUser() {
        User user = userRepository.save(getUser(false));
        UserDto userDto = userMapper.userToDto(user);
        doNothing().when(template).convertAndSend("/topic/users/join", userDto);

        userService.join(userDto);

        user = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.isActive()).isTrue();

        verify(template, times(1))
                .convertAndSend("/topic/users/join", userMapper.userToDto(user));
    }

    @Test
    void testJoinAlreadyJoinedUser() {
        User user = userRepository.save(getUser(true));
        UserDto userDto = userMapper.userToDto(user);
        doNothing().when(template).convertAndSend("/topic/users/join", userDto);

        userService.join(userDto);

        user = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.isActive()).isTrue();

        verify(template, times(0))
                .convertAndSend("/topic/users/join", userMapper.userToDto(user));
    }

    @Test
    void testLeftUser() {
        User user = userRepository.save(getUser(true));
        UserDto userDto = userMapper.userToDto(user);
        doNothing().when(template).convertAndSend("/topic/users/left", userDto);

        userService.left(userDto);

        user = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.isActive()).isFalse();

        verify(template, times(1))
                .convertAndSend("/topic/users/left", userMapper.userToDto(user));
    }

    @Test
    void testLeftNotActiveUser() {
        User user = userRepository.save(getUser(false));
        UserDto userDto = userMapper.userToDto(user);
        doNothing().when(template).convertAndSend("/topic/users/left", userDto);

        userService.left(userDto);

        user = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.isActive()).isFalse();

        verify(template, times(0))
                .convertAndSend("/topic/users/left", userMapper.userToDto(user));
    }

    @Test
    void testLeftNotExistingUser() {
        UserDto userDto = getUserDto();
        doNothing().when(template).convertAndSend("/topic/users/left", userDto);

        userService.left(userDto);

        assertThat(userRepository.findByEmail(userDto.getEmail())).isEmpty();
    }
}