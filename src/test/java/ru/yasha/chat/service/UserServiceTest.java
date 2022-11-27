package ru.yasha.chat.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.yasha.chat.config.AppConfigs;
import ru.yasha.chat.dto.UserDto;
import ru.yasha.chat.entity.User;
import ru.yasha.chat.exception.UserNotFoundException;
import ru.yasha.chat.exception.UsernameAlreadyInUseException;
import ru.yasha.chat.mapper.UserMapper;
import ru.yasha.chat.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @Autowired
    private AppConfigs configs;
    @MockBean
    private SimpMessagingTemplate template;

    @Test
    void testJoinNewUser() {
        UserDto userDto = getUserDto();
        doNothing().when(template).convertAndSend(configs.getUserJoinedTopic(), userDto);

        userService.join(userDto);

        User user = userRepository.findByName(userDto.getName()).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.isActive()).isTrue();

        verify(template, times(1))
                .convertAndSend(configs.getUserJoinedTopic(), userMapper.userToDto(user));
    }

    @Test
    void testJoinExistingInactiveUser() {
        User user = userRepository.save(getUser(false));
        UserDto userDto = userMapper.userToDto(user);
        doNothing().when(template).convertAndSend(configs.getUserJoinedTopic(), userDto);

        userService.join(userDto);

        user = userRepository.findByName(userDto.getName()).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.isActive()).isTrue();

        verify(template, times(1))
                .convertAndSend(configs.getUserJoinedTopic(), userMapper.userToDto(user));
    }

    @Test
    void testJoinUser_UsernameInUse() {
        User user = userRepository.save(getUser(false));
        UserDto userDto = userMapper.userToDto(user);
        userDto.setEmail("another@test.com");
        doNothing().when(template).convertAndSend(configs.getUserJoinedTopic(), userDto);

        assertThrows(UsernameAlreadyInUseException.class, () -> userService.join(userDto));

        user = userRepository.findByName(userDto.getName()).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.isActive()).isFalse();

        verify(template, times(0))
                .convertAndSend(configs.getUserJoinedTopic(), userMapper.userToDto(user));
    }

    @Test
    void testLeaveChat() {
        User user = userRepository.save(getUser(true));
        UserDto userDto = userMapper.userToDto(user);
        doNothing().when(template).convertAndSend(configs.getUserLeftTopic(), userDto);

        userService.leave(userDto);

        user = userRepository.findByName(userDto.getName()).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.isActive()).isFalse();

        verify(template, times(1))
                .convertAndSend(configs.getUserLeftTopic(), userMapper.userToDto(user));
    }

    @Test
    void testInactiveUserLeaveChat() {
        User user = userRepository.save(getUser(false));
        UserDto userDto = userMapper.userToDto(user);
        doNothing().when(template).convertAndSend(configs.getUserLeftTopic(), userDto);

        userService.leave(userDto);

        user = userRepository.findByName(userDto.getName()).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.isActive()).isFalse();

        verify(template, times(0))
                .convertAndSend(configs.getUserLeftTopic(), userMapper.userToDto(user));
    }

    @Test
    void testNotExistingUserLeaveChat() {
        UserDto userDto = getUserDto();
        doNothing().when(template).convertAndSend(configs.getUserLeftTopic(), userDto);

        assertThrows(UserNotFoundException.class, () -> userService.leave(userDto));

        assertThat(userRepository.findByName(userDto.getName())).isEmpty();
    }

    @Test
    void testGetActiveUsers() {
        User userInactive = userRepository.save(getRandomUser(false));
        User userActive = userRepository.save(getRandomUser(true));

        List<UserDto> userList = userService.getActiveUsers();
        assertThat(userList)
                .isNotNull()
                .hasSize(1)
                .contains(userMapper.userToDto(userActive))
                .doesNotContain(userMapper.userToDto(userInactive));
    }

    @Test
    void testGetActiveUser_AllInactive() {
        userRepository.save(getRandomUser(false));

        List<UserDto> userList = userService.getActiveUsers();
        assertThat(userList).isNotNull().isEmpty();
    }
}
