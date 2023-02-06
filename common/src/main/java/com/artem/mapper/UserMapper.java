package com.artem.mapper;

import com.artem.model.dto.UserDto;
import com.artem.model.entity.User;

public class UserMapper implements Mapper<User, UserDto> {

    @Override
    public User toEntity(UserDto dto) {
        return new User(dto.firstName(), dto.lastName());
    }

    @Override
    public UserDto toDto(User entity) {
        return new UserDto(entity.getFirstName(), entity.getLastName());
    }
}
