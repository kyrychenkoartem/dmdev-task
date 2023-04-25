package com.artem.integration.controller.rest;

import com.artem.integration.repository.RepositoryTestBase;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.dto.UserUpdateDto;
import com.artem.model.type.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static com.artem.util.ConstantUtil.USER_1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@AutoConfigureMockMvc
public class UserRestControllerTest extends RepositoryTestBase {

    private static final String URL_PREFIX = "/api/v1/users/";
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.metadata").exists());
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get(URL_PREFIX + USER_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.birthDate").exists())
                .andExpect(jsonPath("$.role").exists());
    }


    @Test
    void create() throws Exception {
        mockMvc.perform(post(URL_PREFIX)
                        .content(asJsonString(getUserCreateDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").exists(),
                        jsonPath("$.firstName").exists(),
                        jsonPath("$.lastName").exists(),
                        jsonPath("$.email").exists(),
                        jsonPath("$.birthDate").exists(),
                        jsonPath("$.role").exists()
                );
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(put(URL_PREFIX + USER_1)
                        .content(asJsonString(getUserUpdateDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").exists(),
                        jsonPath("$.firstName").exists(),
                        jsonPath("$.lastName").exists(),
                        jsonPath("$.email").exists(),
                        jsonPath("$.birthDate").exists(),
                        jsonPath("$.role").exists()
                );
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PREFIX + USER_1))
                .andExpectAll(
                        status().isNoContent()
                );
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
                .email("test2@gmail.com")
                .birthDate(LocalDate.of(2020, 2, 2))
                .role(Role.ADMIN)
                .build();
    }

    private String asJsonString(final Object obj) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
