package com.artem.integration.service;

import com.artem.mapper.UserMapper;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.dto.UserFilter;
import com.artem.model.dto.UserReadDto;
import com.artem.model.dto.UserUpdateDto;
import com.artem.model.entity.User;
import com.artem.model.type.Role;
import com.artem.model.type.UserStatus;
import com.artem.repository.QPredicate;
import com.artem.repository.UserRepository;
import com.artem.service.UserService;
import com.querydsl.core.types.Predicate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static com.artem.model.entity.QUser.user;
import static com.artem.util.ConstantUtil.ALL_3_USERS;
import static com.artem.util.ConstantUtil.PAGE;
import static com.artem.util.ConstantUtil.SIZE;
import static com.artem.util.ConstantUtil.USER_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void checkFindAll() {
        var userList = List.of(getUser(), getUser(), getUser());
        var expectedResponse = List.of(getUserReadDto(), getUserReadDto(), getUserReadDto());
        doReturn(userList).when(userRepository).findAll();
        doReturn(getUserReadDto(), getUserReadDto(), getUserReadDto()).when(userMapper).mapFrom(any(User.class));

        var actualResponse = userService.findAll();

        assertThat(actualResponse).hasSize(ALL_3_USERS);
        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    void checkFindAllWithFilter() {
        var pageable = PageRequest.of(PAGE, SIZE);
        var filter = new UserFilter("Te", "Te",
                LocalDate.of(2000, 2, 1));
        var predicate = getPredicate(filter);
        Page<User> users = new PageImpl<>(List.of(getUser(), getUser(), getUser()), pageable, SIZE);
        Page<UserReadDto> expectedResponse = new PageImpl<>(List.of(
                getUserReadDto(), getUserReadDto(), getUserReadDto()), pageable, SIZE);
        doReturn(users).when(userRepository).findAll(predicate, pageable);
        doReturn(getUserReadDto(), getUserReadDto(), getUserReadDto()).when(userMapper).mapFrom(any(User.class));

        var actualResponse = userService.findAll(filter, pageable);

        assertThat(actualResponse).hasSize(ALL_3_USERS);
        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    void checkFindById() {
        var user = getUser();
        var expectedResponse = getUserReadDto();
        doReturn(Optional.of(user)).when(userRepository).findById(USER_1);
        doReturn(expectedResponse).when(userMapper).mapFrom(user);

        var actualResponse = userService.findById(USER_1);

        assertThat(Optional.of(expectedResponse)).isEqualTo(actualResponse);
    }

    @Test
    void checkCreate() {
        var userCreateDto = getUserCreateDto();
        var user = getUser();
        var expectedResponse = getUserReadDto();
        doReturn(user).when(userMapper).mapFrom(userCreateDto);
        doReturn(user).when(userRepository).save(user);
        doReturn(expectedResponse).when(userMapper).mapFrom(user);

        var actualResponse = userService.create(userCreateDto);

        assertThat(actualResponse.id()).isNotNull();
        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    void checkUpdate() {
        var user = getUser();
        var userUpdateDto = getUserUpdateDto();
        var updatedUser = getUpdatedUser();
        var expectedResponse = getUpdatedUserReadDto();
        doReturn(Optional.of(user)).when(userRepository).findById(USER_1);
        doReturn(updatedUser).when(userMapper).mapFrom(user, userUpdateDto);
        doReturn(updatedUser).when(userRepository).saveAndFlush(updatedUser);
        doReturn(expectedResponse).when(userMapper).mapFrom(updatedUser);

        var actualResponse = userService.update(USER_1, userUpdateDto);

        assertThat(Optional.of(expectedResponse)).isEqualTo(actualResponse);
    }

    @Test
    void checkDelete() {
        var user = getUser();
        doReturn(Optional.of(user)).when(userRepository).findById(USER_1);

        var expectedResult = userService.delete(USER_1);

        assertTrue(expectedResult);
    }

    private User getUser() {
        return User.builder()
                .id(USER_1)
                .firstName("Test")
                .lastName("Test")
                .email("test@gmail.com")
                .password("testPassword")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();
    }

    private User getUpdatedUser() {
        return User.builder()
                .id(USER_1)
                .firstName("Test1")
                .lastName("Test1")
                .email("test1@gmail.com")
                .password("testPassword")
                .birthDate(LocalDate.of(2020, 2, 2))
                .role(Role.ADMIN)
                .status(UserStatus.ACTIVE)
                .build();
    }

    private UserReadDto getUserReadDto() {
        return UserReadDto.builder()
                .id(USER_1)
                .firstName("Test")
                .lastName("Test")
                .email("test@gmail.com")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .build();
    }

    private UserReadDto getUpdatedUserReadDto() {
        return UserReadDto.builder()
                .id(USER_1)
                .firstName("Test1")
                .lastName("Test1")
                .email("test1@gmail.com")
                .birthDate(LocalDate.of(2020, 2, 2))
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

    private UserUpdateDto getUserUpdateDto() {
        return UserUpdateDto.builder()
                .firstName("Test1")
                .lastName("Test1")
                .email("test1@gmail.com")
                .birthDate(LocalDate.of(2020, 2, 2))
                .role(Role.ADMIN)
                .build();
    }

    private Predicate getPredicate(UserFilter filter) {
        return QPredicate.builder()
                .add(filter.firstName(), user.firstName::containsIgnoreCase)
                .add(filter.lastName(), user.lastName::containsIgnoreCase)
                .add(filter.birthDate(), user.birthDate::before)
                .buildAnd();
    }
}