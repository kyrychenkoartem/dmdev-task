package com.artem.integration.controller;

import com.artem.integration.repository.RepositoryTestBase;
import com.artem.model.dto.BankCardCreateDto;
import com.artem.model.dto.BankCardReadDto;
import com.artem.model.type.BankType;
import com.artem.model.type.CardType;
import com.artem.service.BankCardService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static com.artem.util.ConstantUtil.BANK_ACCOUNT_ID;
import static com.artem.util.ConstantUtil.BANK_CARD_1;
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
public class BankCardControllerTest extends RepositoryTestBase {

    private static final String URL_PREFIX = "/bank-cards/";
    private final MockMvc mockMvc;
    private final BankCardService bankCardService;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bank-card/bank-cards"))
                .andExpect(model().attributeExists("bankCards"));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get(URL_PREFIX + BANK_CARD_1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bank-card/bank-card"))
                .andExpect(model().attributeExists("bankCard"))
                .andExpect(model().attribute("bankCard", getBankCardReadDto()));
    }

    @Test
    void registration() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(BANK_ACCOUNT_ID, bankCardService.findByUserId(USER_1).get().bankAccountId());
        mockMvc.perform(get(URL_PREFIX + REGISTRATION).session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bank-card/registration"))
                .andExpect(model().attributeExists("card"))
                .andExpect(model().attributeExists("banks"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attribute("card", BankCardCreateDto.builder().build()))
                .andExpect(model().attribute("banks", BankType.values()))
                .andExpect(model().attribute("types", CardType.values()));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post(URL_PREFIX).with(csrf())
                        .param("userId", "1")
                        .param("bankAccountId", "1")
                        .param("cardNumber", "1234567890123457")
                        .param("expiryDate", "12/32")
                        .param("cvv", "345")
                        .param("bank", "CIBC")
                        .param("type", "DEBIT")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/bank-cards/{\\d+}")
                );
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post("/bank-cards/1/update").with(csrf())
                        .param("bankAccountId", "2")
                        .param("expiryDate", "10/33")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/bank-cards/1")
                );
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(post("/bank-cards/1/delete").with(csrf()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/bank-cards")
                );
    }

    private BankCardReadDto getBankCardReadDto() {
        return bankCardService.findByUserId(USER_1).get();
    }
}
