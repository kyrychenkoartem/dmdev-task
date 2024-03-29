package com.artem.integration.controller;

import com.artem.integration.repository.RepositoryTestBase;
import com.artem.model.dto.AccountReadDto;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.dto.UserReadDto;
import com.artem.model.type.Role;
import com.artem.service.AccountService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.artem.util.ConstantUtil.REGISTRATION;
import static com.artem.util.ConstantUtil.USER_1;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class UserControllerTest extends RepositoryTestBase {

    private static final String URL_PREFIX = "/users/";
    private final MockMvc mockMvc;
    private final AccountService accountService;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("filter"));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get(URL_PREFIX + USER_1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/user"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attribute("roles", Role.values()))
                .andExpect(model().attribute("user", getUserReadDto()))
                .andExpect(model().attribute("account", getAccountReadDto()));
    }

    @Test
    void registration() throws Exception {
        mockMvc.perform(get(URL_PREFIX + REGISTRATION))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/registration"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attribute("roles", Role.values()))
                .andExpect(model().attribute("user", UserCreateDto.builder().build()));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post(URL_PREFIX).with(csrf())
                        .param("firstname", "Test")
                        .param("lastname", "Test")
                        .param("email", "test@gmail.com")
                        .param("password", "testPassword")
                        .param("birthDate", "2000-01-01")
                        .param("role", "ADMIN")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/login/")
                );
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post("/users/1/update").with(csrf())
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
    void delete() throws Exception {
        mockMvc.perform(post("/users/1/delete").with(csrf()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users")
                );
    }

    private AccountReadDto getAccountReadDto() {
        return accountService.findByUserId(USER_1).get();
    }

    private UserReadDto getUserReadDto() {
        return UserReadDto.builder()
                .id(USER_1)
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivan@gmail.com")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.ADMIN)
                .build();
    }
}