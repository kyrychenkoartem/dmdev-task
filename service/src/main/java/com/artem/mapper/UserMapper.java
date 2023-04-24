package com.artem.mapper;

import com.artem.model.dto.UserCreateDto;
import com.artem.model.dto.UserReadDto;
import com.artem.model.dto.UserUpdateDto;
import com.artem.model.entity.User;
import com.artem.model.type.UserStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<UserCreateDto, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User mapFrom(UserCreateDto createDto) {
        var user = User.builder()
                .firstName(createDto.firstname())
                .lastName(createDto.lastname())
                .email(createDto.email())
                .birthDate(createDto.birthDate())
                .role(createDto.role())
                .status(UserStatus.ACTIVE)
                .build();
        Optional.ofNullable(createDto.password())
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .ifPresent(user::setPassword);
        Optional.ofNullable(createDto.image())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> user.setImage(image.getOriginalFilename()));
        return user;
    }

    public UserReadDto mapFrom(User user) {
        return UserReadDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .role(user.getRole())
                .image(user.getImage())
                .build();
    }

    public User mapFrom(User user, UserUpdateDto updateDto) {
        user.setFirstName(updateDto.firstName());
        user.setLastName(updateDto.lastName());
        user.setEmail(updateDto.email());
        user.setBirthDate(updateDto.birthDate());
        user.setRole(updateDto.role());
        Optional.ofNullable(updateDto.image())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> user.setImage(image.getOriginalFilename()));
        return user;
    }
}
