package com.artem.integration.controller;

import com.artem.integration.repository.RepositoryTestBase;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.BankAccountReadDto;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.artem.util.ConstantUtil.BANK_ACCOUNT_1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RequiredArgsConstructor
@AutoConfigureMockMvc
public class BankAccountControllerTest extends RepositoryTestBase {

    private final MockMvc mockMvc;
    private final BankAccountService bankAccountService;

    @Test
    @SneakyThrows
    void findAll() {
        mockMvc.perform(get("/bank-accounts"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bank-account/bank-accounts"))
                .andExpect(model().attributeExists("bankAccounts"));
    }

    @Test
    @SneakyThrows
    void findById() {
        mockMvc.perform(get("/bank-accounts/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bank-account/bank-account"))
                .andExpect(model().attributeExists("bankAccount"))
                .andExpect(model().attributeExists("statuses"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attribute("statuses", AccountStatus.values()))
                .andExpect(model().attribute("types", AccountType.values()))
                .andExpect(model().attribute("bankAccount", getBankAccountReadDto()));
    }

    @Test
    @SneakyThrows
    void registration() {
        mockMvc.perform(get("/bank-accounts/registration"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bank-account/registration"))
                .andExpect(model().attributeExists("bankAccount"))
                .andExpect(model().attributeExists("statuses"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attribute("statuses", AccountStatus.values()))
                .andExpect(model().attribute("types", AccountType.values()))
                .andExpect(model().attribute("bankAccount", BankAccountCreateDto.builder().build()));
    }

    @Test
    @SneakyThrows
    void create() {
        mockMvc.perform(post("/bank-accounts")
                        .param("accountId", "1")
                        .param("number", "123456789554645")
                        .param("type", "SAVINGS_ACCOUNT")
                        .param("status", "ACTIVE")
                        .param("availableBalance", "200.00")
                        .param("actualBalance", "100.00")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/bank-accounts/{\\d+}")
                );
    }

    @Test
    @SneakyThrows
    void update() {
        mockMvc.perform(post("/bank-accounts/1/update")
                        .param("accountType", "FIXED_DEPOSIT")
                        .param("accountStatus", "BLOCKED")
                        .param("availableBalance", "300.00")
                        .param("actualBalance", "200.00")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/bank-accounts/1")
                );
    }

    @Test
    @SneakyThrows
    void delete() {
        mockMvc.perform(post("/bank-accounts/1/delete"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/bank-accounts")
                );
    }

    private BankAccountReadDto getBankAccountReadDto() {
        return bankAccountService.findById(BANK_ACCOUNT_1).get();
    }
}