package com.artem.integration.controller;

import com.artem.integration.repository.RepositoryTestBase;
import com.artem.model.dto.UtilityAccountCreateDto;
import com.artem.model.dto.UtilityAccountReadDto;
import com.artem.service.UtilityAccountService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.artem.util.ConstantUtil.REGISTRATION;
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

    private static final String URL_PREFIX = "/utility-accounts/";
    private final MockMvc mockMvc;
    private final UtilityAccountService utilityAccountService;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("utility-account/utility-accounts"))
                .andExpect(model().attributeExists("utilityAccounts"));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get(URL_PREFIX + UTILITY_ACCOUNT_1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("utility-account/utility-account"))
                .andExpect(model().attributeExists("utilityAccount"))
                .andExpect(model().attribute("utilityAccount", getUtilityAccountReadDto()));
    }

    @Test
    void registration() throws Exception {
        mockMvc.perform(get(URL_PREFIX + REGISTRATION))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("utility-account/registration"))
                .andExpect(model().attributeExists("utilityAccount"))
                .andExpect(model().attribute("utilityAccount", UtilityAccountCreateDto.builder().build()));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post(URL_PREFIX)
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
    void update() throws Exception {
        mockMvc.perform(post("/utility-accounts/1/update")
                        .param("providerName", "UpdatedName")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/utility-accounts/1")
                );
    }

    @Test
    void delete() throws Exception {
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
