package com.artem.dao;

import com.artem.mapper.UserMapper;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.dto.UserUpdateDto;
import com.artem.model.type.Role;
import com.artem.model.type.UserStatus;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_USERS;
import static com.artem.util.ConstantUtil.USER_ID_ONE;
import static com.artem.util.ConstantUtil.USER_ID_TWO;
import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest extends RepositoryTestBase {

    private final UserRepository userRepository = new UserRepository(session);
    private final UserMapper userMapper = new UserMapper();

    @Test
    void checkSaveUser() {
        var userCreateDto = getUserCreateDto();
        var expectedUser = userMapper.mapFrom(userCreateDto);

        var actualUser = userRepository.save(expectedUser);
        session.clear();

        assertThat(userRepository.findById(actualUser.getId()).get().getId()).isNotNull();
    }

    @Test
    void checkUserDelete() {
        var actualUser = userRepository.findById(USER_ID_TWO);
        actualUser.get().setStatus(UserStatus.DELETED);

        userRepository.delete(actualUser.get());


        assertThat(userRepository.findById(USER_ID_TWO).get().getStatus()).isEqualTo(UserStatus.DELETED);
    }

    @Test
    void checkUpdateUser() {
        var maybeUser = userRepository.findById(USER_ID_ONE);
        var updateDto = getUserUpdateDto();
        var expectedUser = userMapper.mapFrom(maybeUser.get(), updateDto);

        userRepository.update(expectedUser);
        session.clear();
        var actualUser = userRepository.findById(maybeUser.get().getId()).get();

        assertThat(actualUser.getFirstName()).isEqualTo("Test");
        assertThat(actualUser.getLastName()).isEqualTo("Test");
        assertThat(actualUser.getPassword()).isEqualTo("updatedPassword");
        assertThat(actualUser.getRole()).isEqualTo(Role.ADMIN);
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
