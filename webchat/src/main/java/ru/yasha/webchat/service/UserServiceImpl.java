package ru.yasha.webchat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yasha.webchat.dto.UserDto;
import ru.yasha.webchat.entity.User;
import ru.yasha.webchat.mapper.UserMapper;
import ru.yasha.webchat.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void join(UserDto userDto) {
        log.info("Joining user {}", userDto.getEmail());
        User user = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        if (user != null) {
            if (user.isActive()) return;
            user.setActive(true);
        } else {
            user = userMapper.dtoToUser(userDto);
            user.setActive(true);
            userRepository.save(user);
        }
        messagingTemplate.convertAndSend("/topic/users/join", userMapper.userToDto(user));
    }

    @Override
    @Transactional
    public void left(UserDto userDto) {
        log.info("User left {}", userDto.getEmail());
        User user = userRepository.findByEmail(userDto.getEmail()).orElse(null); // TODO - or exception
        if (user != null && user.isActive()) {
            user.setActive(false);
            messagingTemplate.convertAndSend("/topic/users/left", userMapper.userToDto(user));
        }
    }

    @Override
    public List<UserDto> getActiveUsers() {
        return userRepository.findByActive(true)
                .stream().map(userMapper::userToDto)
                .toList();
    }
}
