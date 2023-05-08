package com.artem.integration.controller;

import com.artem.integration.repository.RepositoryTestBase;
import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.AccountReadDto;
import com.artem.model.type.AccountStatus;
import com.artem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.artem.util.ConstantUtil.ACCOUNT_1;
import static com.artem.util.ConstantUtil.REGISTRATION;
import static com.artem.util.ConstantUtil.USER_1;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
    private static final String URL_PREFIX = "/accounts/";
    private final MockMvc mockMvc;
    private final AccountService accountService;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("account/accounts"))
                .andExpect(model().attributeExists("accounts"));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get(URL_PREFIX + ACCOUNT_1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("account/account"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("statuses"))
                .andExpect(model().attribute("statuses", AccountStatus.values()))
                .andExpect(model().attribute("account", getAccountReadDto()));
    }

    @Test
    void registration() throws Exception {
        mockMvc.perform(get(URL_PREFIX + REGISTRATION))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("account/registration"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("statuses"))
                .andExpect(model().attribute("statuses", AccountStatus.values()))
                .andExpect(model().attribute("account", AccountCreateDto.builder().build()));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post(URL_PREFIX).with(csrf())
                        .param("userId", "6")
                        .param("status", "ACTIVE")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/accounts/{\\d+}")
                );
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post("/accounts/1/update").with(csrf())
                        .param("status", "BLOCKED")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/accounts/1")
                );
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(post("/accounts/1/delete").with(csrf()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/accounts")
                );
    }

    private AccountReadDto getAccountReadDto() {
        return accountService.findByUserId(USER_1).get();
    }
}
