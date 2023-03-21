package com.artem.service;

import com.artem.database.UserRepository;
import com.artem.mapper.UserMapper;

public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
}
