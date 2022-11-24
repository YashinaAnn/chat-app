package com.example.demo.service;

import com.example.demo.client.UserClient;
import com.example.demo.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserClient userClient;

    @Override
    public List<UserDto> getActiveUsers() {
        return userClient.getActiveUsers();
    }
}
