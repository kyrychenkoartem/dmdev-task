package com.artem.mapper;

import com.artem.model.dto.UserCreateDto;
import com.artem.model.dto.UserReadDto;
import com.artem.model.dto.UserUpdateDto;
import com.artem.model.entity.User;
import com.artem.model.type.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserCreateDto, User> {
    @Override
    public User mapFrom(UserCreateDto createDto) {
        return User.builder()
                .firstName(createDto.firstname())
                .lastName(createDto.lastname())
                .email(createDto.email())
                .password(createDto.password())
                .birthDate(createDto.birthDate())
                .role(createDto.role())
                .status(UserStatus.ACTIVE)
                .build();
    }

    public UserReadDto mapFrom(User user) {
        return UserReadDto.builder()
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .role(user.getRole())
                .build();
    }

    public User mapFrom(User user, UserUpdateDto updateDto) {
        user.setFirstName(updateDto.firstname());
        user.setLastName(updateDto.lastname());
        user.setPassword(updateDto.password());
        user.setRole(updateDto.role());
        return user;
    }
}
