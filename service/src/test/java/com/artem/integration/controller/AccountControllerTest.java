package com.artem.integration.controller;

import com.artem.integration.repository.RepositoryTestBase;
import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.AccountReadDto;
import com.artem.model.type.AccountStatus;
import com.artem.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.artem.util.ConstantUtil.ACCOUNT_1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RequiredArgsConstructor
@AutoConfigureMockMvc
public class AccountControllerTest extends RepositoryTestBase {

    private final MockMvc mockMvc;
    private final AccountService accountService;

    @Test
    @SneakyThrows
    void findAll() {
        mockMvc.perform(get("/accounts"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("account/accounts"))
                .andExpect(model().attributeExists("accounts"));
    }

    @Test
    @SneakyThrows
    void findById() {
        mockMvc.perform(get("/accounts/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("account/account"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("statuses"))
                .andExpect(model().attribute("statuses", AccountStatus.values()))
                .andExpect(model().attribute("account", getAccountReadDto()));
    }

    @Test
    @SneakyThrows
    void registration() {
        mockMvc.perform(get("/accounts/registration"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("account/registration"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("statuses"))
                .andExpect(model().attribute("statuses", AccountStatus.values()))
                .andExpect(model().attribute("account", AccountCreateDto.builder().build()));
    }

    @Test
    @SneakyThrows
    void create() {
        mockMvc.perform(post("/accounts")
                        .param("userId", "6")
                        .param("status", "ACTIVE")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/accounts/{\\d+}")
                );
    }

    @Test
    @SneakyThrows
    void update() {
        mockMvc.perform(post("/accounts/1/update")
                        .param("status", "BLOCKED")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/accounts/1")
                );
    }

    @Test
    @SneakyThrows
    void delete() {
        mockMvc.perform(post("/accounts/1/delete"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/accounts")
                );
    }

    private AccountReadDto getAccountReadDto() {
        return accountService.findById(ACCOUNT_1).get();
    }
}
