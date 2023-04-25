package com.artem.integration.service;

import com.artem.mapper.BankCardMapper;
import com.artem.model.dto.BankCardCreateDto;
import com.artem.model.dto.BankCardReadDto;
import com.artem.model.dto.BankCardUpdateDto;
import com.artem.model.entity.Account;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.BankCard;
import com.artem.model.entity.User;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.BankType;
import com.artem.model.type.CardType;
import com.artem.model.type.Role;
import com.artem.model.type.UserStatus;
import com.artem.repository.BankCardRepository;
import com.artem.service.BankCardService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.artem.util.ConstantUtil.ACCOUNT_1;
import static com.artem.util.ConstantUtil.ALL_3_BANK_CARDS;
import static com.artem.util.ConstantUtil.BANK_ACCOUNT_1;
import static com.artem.util.ConstantUtil.BANK_ACCOUNT_2;
import static com.artem.util.ConstantUtil.BANK_CARD_1;
import static com.artem.util.ConstantUtil.CARD_NUMBER;
import static com.artem.util.ConstantUtil.EXPIRY_DATE;
import static com.artem.util.ConstantUtil.UPDATED_EXPIRY_DATE;
import static com.artem.util.ConstantUtil.USER_1;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class BankCardServiceTest {

    @Mock
    private BankCardRepository bankCardRepository;

    @Mock
    private BankCardMapper bankCardMapper;

    @InjectMocks
    private BankCardService bankCardService;

    @Test
    void checkFindAll() {
        var bankCardList = List.of(getBankCard(), getBankCard(), getBankCard());
        var expectedResponse = List.of(getBankCardReadDto(), getBankCardReadDto(), getBankCardReadDto());
        doReturn(bankCardList).when(bankCardRepository).findAll();
        doReturn(getBankCardReadDto(), getBankCardReadDto(), getBankCardReadDto())
                .when(bankCardMapper).mapFrom(any(BankCard.class));

        var actualResponse = bankCardService.findAll();

        assertThat(actualResponse).hasSize(ALL_3_BANK_CARDS);
        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    void checkFindById() {
        var bankCard = getBankCard();
        var expectedResponse = getBankCardReadDto();
        doReturn(Optional.of(bankCard)).when(bankCardRepository).findById(BANK_CARD_1);
        doReturn(expectedResponse).when(bankCardMapper).mapFrom(bankCard);

        var actualResponse = bankCardService.findById(BANK_CARD_1);

        assertThat(Optional.of(expectedResponse)).isEqualTo(actualResponse);
    }

    @Test
    void checkCreate() {
        var bankCardCreateDto = getBankCardCreateDto();
        var bankCard = getBankCard();
        var expectedResponse = getBankCardReadDto();
        doReturn(bankCard).when(bankCardMapper).mapFrom(bankCardCreateDto);
        doReturn(bankCard).when(bankCardRepository).save(bankCard);
        doReturn(expectedResponse).when(bankCardMapper).mapFrom(bankCard);

        var actualResponse = bankCardService.create(bankCardCreateDto);

        assertThat(actualResponse.id()).isNotNull();
        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    void checkUpdate() {
        var bankCard = getBankCard();
        var bankCardUpdateDto = getBankCardUpdateDto();
        var updatedBankCard = getUpdatedBankCard();
        var expectedResponse = getUpdatedBankCardReadDto();
        doReturn(Optional.of(bankCard)).when(bankCardRepository).findById(BANK_CARD_1);
        doReturn(updatedBankCard).when(bankCardMapper).mapFrom(bankCard, bankCardUpdateDto);
        doReturn(updatedBankCard).when(bankCardRepository).saveAndFlush(updatedBankCard);
        doReturn(expectedResponse).when(bankCardMapper).mapFrom(updatedBankCard);

        var actualResponse = bankCardService.update(BANK_CARD_1, bankCardUpdateDto);

        assertThat(Optional.of(expectedResponse)).isEqualTo(actualResponse);
    }

    @Test
    void checkDelete() {
        var bankCard = getBankCard();
        doReturn(Optional.of(bankCard)).when(bankCardRepository).findById(BANK_CARD_1);

        var expectedResult = bankCardService.delete(BANK_CARD_1);

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

    private Account getAccount() {
        return Account.builder()
                .id(ACCOUNT_1)
                .user(getUser())
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2000, 1, 1, 0, 0).truncatedTo(DAYS))
                .createdBy(getUser().getEmail())
                .build();
    }

    private BankAccount getBankAccount() {
        return BankAccount.builder()
                .id(BANK_ACCOUNT_1)
                .number("123456")
                .type(AccountType.CHECKING_ACCOUNT)
                .status(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(200).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(100).setScale(2, RoundingMode.CEILING))
                .account(getAccount())
                .build();
    }

    private BankAccount getBankAccount2() {
        return BankAccount.builder()
                .id(BANK_ACCOUNT_2)
                .number("234567")
                .type(AccountType.SAVINGS_ACCOUNT)
                .status(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(200).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(100).setScale(2, RoundingMode.CEILING))
                .account(getAccount())
                .build();
    }

    private BankCardCreateDto getBankCardCreateDto() {
        return BankCardCreateDto.builder()
                .userId(getUser().getId())
                .bankAccountId(getBankAccount().getId())
                .cardNumber(CARD_NUMBER)
                .expiryDate(EXPIRY_DATE)
                .cvv("123")
                .bank(BankType.CIBC)
                .type(CardType.DEBIT)
                .build();
    }

    private BankCard getBankCard() {
        return BankCard.builder()
                .id(BANK_CARD_1)
                .cardNumber(CARD_NUMBER)
                .expiryDate(EXPIRY_DATE)
                .bank(BankType.CIBC)
                .cvv("123")
                .cardType(CardType.DEBIT)
                .user(getUser())
                .bankAccount(getBankAccount())
                .build();
    }

    private BankCardReadDto getBankCardReadDto() {
        return BankCardReadDto.builder()
                .id(BANK_CARD_1)
                .userId(USER_1)
                .bankAccountId(BANK_ACCOUNT_1)
                .cardNumber(CARD_NUMBER)
                .expiryDate(EXPIRY_DATE)
                .bank(BankType.CIBC)
                .cvv("123")
                .build();
    }

    private BankCardUpdateDto getBankCardUpdateDto() {
        return BankCardUpdateDto.builder()
                .bankAccountId(BANK_ACCOUNT_2)
                .expiryDate(UPDATED_EXPIRY_DATE)
                .build();
    }

    private BankCard getUpdatedBankCard() {
        return BankCard.builder()
                .id(BANK_CARD_1)
                .cardNumber(CARD_NUMBER)
                .expiryDate(UPDATED_EXPIRY_DATE)
                .bank(BankType.CIBC)
                .cvv("123")
                .cardType(CardType.DEBIT)
                .user(getUser())
                .bankAccount(getBankAccount2())
                .build();
    }

    private BankCardReadDto getUpdatedBankCardReadDto() {
        return BankCardReadDto.builder()
                .id(BANK_CARD_1)
                .userId(USER_1)
                .bankAccountId(BANK_ACCOUNT_2)
                .cardNumber(CARD_NUMBER)
                .expiryDate(UPDATED_EXPIRY_DATE)
                .bank(BankType.CIBC)
                .cvv("123")
                .build();
    }
}
