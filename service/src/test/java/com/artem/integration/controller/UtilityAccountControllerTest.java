package com.artem.integration.controller;

import com.artem.integration.repository.RepositoryTestBase;
import com.artem.model.dto.UtilityAccountCreateDto;
import com.artem.model.dto.UtilityAccountReadDto;
import com.artem.service.UtilityAccountService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.artem.util.ConstantUtil.UTILITY_ACCOUNT_1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RequiredArgsConstructor
@AutoConfigureMockMvc
public class UtilityAccountControllerTest extends RepositoryTestBase {

    private final MockMvc mockMvc;
    private final UtilityAccountService utilityAccountService;

    @Test
    @SneakyThrows
    void findAll() {
        mockMvc.perform(get("/utility-accounts"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("utility-account/utility-accounts"))
                .andExpect(model().attributeExists("utilityAccounts"));
    }

    @Test
    @SneakyThrows
    void findById() {
        mockMvc.perform(get("/utility-accounts/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("utility-account/utility-account"))
                .andExpect(model().attributeExists("utilityAccount"))
                .andExpect(model().attribute("utilityAccount", getUtilityAccountReadDto()));
    }

    @Test
    @SneakyThrows
    void registration() {
        mockMvc.perform(get("/utility-accounts/registration"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("utility-account/registration"))
                .andExpect(model().attributeExists("utilityAccount"))
                .andExpect(model().attribute("utilityAccount", UtilityAccountCreateDto.builder().build()));
    }

    @Test
    @SneakyThrows
    void create() {
        mockMvc.perform(post("/utility-accounts")
                        .param("accountId", "1")
                        .param("number", "2445464")
                        .param("providerName", "BOOM")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/utility-accounts/{\\d+}")
                );
    }

    @Test
    @SneakyThrows
    void update() {
        mockMvc.perform(post("/utility-accounts/1/update")
                        .param("providerName", "UpdatedName")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/utility-accounts/1")
                );
    }

    @Test
    @SneakyThrows
    void delete() {
        mockMvc.perform(post("/utility-accounts/1/delete"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/utility-accounts")
                );
    }

    private UtilityAccountReadDto getUtilityAccountReadDto() {
        return utilityAccountService.findById(UTILITY_ACCOUNT_1).get();
    }
}
