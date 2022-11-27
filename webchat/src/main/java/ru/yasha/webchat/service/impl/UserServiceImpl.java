package ru.yasha.webchat.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yasha.webchat.dto.UserDto;
import ru.yasha.webchat.entity.User;
import ru.yasha.webchat.exception.UsernameAlreadyInUseException;
import ru.yasha.webchat.mapper.UserMapper;
import ru.yasha.webchat.repository.UserRepository;
import ru.yasha.webchat.service.UserService;

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
    public UserDto join(UserDto userDto) {
        log.debug("Joining user {}", userDto.getName());
        User user = userRepository.findByName(userDto.getName()).orElse(null);
        if (user != null) {
            if (user.getEmail().equals(userDto.getEmail())) {
                user.setActive(true);
            } else {
                throw new UsernameAlreadyInUseException("Username already in use, please select another");
            }
        } else {
            user = userMapper.dtoToUser(userDto);
            user.setActive(true);
            userRepository.save(user);
        }
        userDto = userMapper.userToDto(user);
        messagingTemplate.convertAndSend("/topic/users/join", userDto);
        return userDto;
    }

    @Override
    @Transactional
    public void leave(UserDto userDto) {
        log.debug("Disconnecting users {}", userDto.getName());
        User user = userRepository.findByName(userDto.getName()).orElse(null);
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
