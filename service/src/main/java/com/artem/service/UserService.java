package com.artem.service;

import com.artem.database.UserRepository;
import com.artem.mapper.UserMapper;
import com.artem.model.dto.UserDto;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository = new UserRepository();
    private final UserMapper userMapper = new UserMapper();

    public Optional<UserDto> findByName(String name) {
        return userRepository.findByName(name)
                .map(userMapper::toDto);
    }
}
