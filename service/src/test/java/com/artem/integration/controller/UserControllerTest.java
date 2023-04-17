package com.artem.integration.controller;

import com.artem.integration.repository.RepositoryTestBase;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.dto.UserReadDto;
import com.artem.model.type.Role;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.artem.util.ConstantUtil.USER_1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class UserControllerTest extends RepositoryTestBase {

    private final MockMvc mockMvc;

    @Test
    @SneakyThrows
    void findAll() {
        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("filter"));
    }

    @Test
    @SneakyThrows
    void findById() {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/user"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attribute("roles", Role.values()))
                .andExpect(model().attribute("user", getUserReadDto()));
    }

    @Test
    @SneakyThrows
    void registration() {
        mockMvc.perform(get("/users/registration"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/registration"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attribute("roles", Role.values()))
                .andExpect(model().attribute("user", UserCreateDto.builder().build()));
    }

    @Test
    @SneakyThrows
    void create() {
        mockMvc.perform(post("/users")
                        .param("firstname", "Test")
                        .param("lastname", "Test")
                        .param("email", "test@gmail.com")
                        .param("password", "testPassword")
                        .param("birthDate", "2000-01-01")
                        .param("role", "ADMIN")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/users/{\\d+}")
                );
    }

    @Test
    @SneakyThrows
    void update() {
        mockMvc.perform(post("/users/1/update")
                        .param("firstName", "Test1")
                        .param("lastName", "Test1")
                        .param("email", "test2@gmail.com")
                        .param("birthDate", "2001-01-01")
                        .param("role", "USER")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users/1")
                );
    }

    @Test
    @SneakyThrows
    void delete() {
        mockMvc.perform(post("/users/1/delete"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users")
                );
    }

    private UserReadDto getUserReadDto() {
        return UserReadDto.builder()
                .id(USER_1)
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivan@gmail.com")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .build();
    }
}