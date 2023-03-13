package com.artem.dao;

import com.artem.mapper.UserMapper;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.dto.UserUpdateDto;
import com.artem.model.type.Role;
import com.artem.model.type.UserStatus;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_USERS;
import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTestIT extends RepositoryTestBase {

    private final UserRepository userRepository = new UserRepository(session);
    private final UserMapper userMapper = new UserMapper();

    @Test
    void checkSaveUser() {
        var userCreateDto = getUserCreateDto();
        var expectedUser = userMapper.mapFrom(userCreateDto);

        var actualUser = userRepository.save(expectedUser);

        assertThat(actualUser.getId()).isNotNull();
    }

    @Test
    void checkUserDelete() {
        var actualUser = userRepository.findById(2L);
        actualUser.get().setStatus(UserStatus.DELETED);

        userRepository.delete(actualUser.get());

        assertThat(userRepository.findById(2L).get().getStatus()).isEqualTo(UserStatus.DELETED);
    }

    @Test
    void checkUpdateUser() {
        var actualUser = userRepository.findById(1L);
        var updateDto = getUserUpdateDto();
        var expectedUser = userMapper.mapFrom(actualUser.get(), updateDto);

        userRepository.update(expectedUser);
        session.clear();

        assertThat(userRepository.findById(actualUser.get().getId()).get().getFirstName()).isEqualTo("Test");
        assertThat(userRepository.findById(actualUser.get().getId()).get().getLastName()).isEqualTo("Test");
        assertThat(userRepository.findById(actualUser.get().getId()).get().getPassword()).isEqualTo("updatedPassword");
        assertThat(userRepository.findById(actualUser.get().getId()).get().getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void checkUserFindById() {
        var userCreateDto = getUserCreateDto();
        var user = userMapper.mapFrom(userCreateDto);
        var expectedUser = userRepository.save(user);
        session.clear();

        var actualUser = userRepository.findById(expectedUser.getId());

        assertThat(expectedUser).isEqualTo(actualUser.get());
    }

    @Test
    void checkFindAllUsers() {
        var userList = userRepository.findAll();

        assertThat(userList.size()).isEqualTo(ALL_USERS);
    }

    private UserUpdateDto getUserUpdateDto() {
        return UserUpdateDto.builder()
                .firstname("Test")
                .lastname("Test")
                .password("updatedPassword")
                .role(Role.ADMIN)
                .build();
    }

    private UserCreateDto getUserCreateDto() {
        return UserCreateDto.builder()
                .firstname("Test")
                .lastname("Test")
                .email("test@gmail.com")
                .password("testPassword")
                .birthDate(LocalDate.of(2023, 3, 11))
                .role(Role.USER)
                .build();
    }
}
