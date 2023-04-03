package com.artem.integration.dao;

import com.artem.dao.UserRepository;
import com.artem.mapper.UserMapper;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.dto.UserUpdateDto;
import com.artem.model.type.Role;
import com.artem.model.type.UserStatus;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_USERS;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class UserRepositoryTest extends RepositoryTestBase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EntityManager session;

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
        var userCreateDto = getUserCreateDto();
        var expectedUser = userMapper.mapFrom(userCreateDto);
        var user = userRepository.save(expectedUser);
        session.clear();
        var actualUser = userRepository.findById(user.getId());
        actualUser.get().setStatus(UserStatus.DELETED);

        userRepository.deleteUser(actualUser.get());

        assertThat(userRepository.findById(actualUser.get().getId()).get().getStatus()).isEqualTo(UserStatus.DELETED);
    }

    @Test
    void checkUpdateUser() {
        var userCreateDto = getUserCreateDto();
        var userToSave = userMapper.mapFrom(userCreateDto);
        var user = userRepository.save(userToSave);
        session.clear();
        var maybeUser = userRepository.findById(user.getId());
        var updateDto = getUserUpdateDto();
        var expectedUser = userMapper.mapFrom(maybeUser.get(), updateDto);

        userRepository.saveAndFlush(expectedUser);
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
