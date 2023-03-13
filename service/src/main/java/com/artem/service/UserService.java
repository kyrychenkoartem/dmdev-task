package com.artem.service;

import com.artem.database.UserRepository;
import com.artem.mapper.UserMapper;

public class UserService {

    private final UserRepository userRepository = new UserRepository();
    private final UserMapper userMapper = new UserMapper();

}
