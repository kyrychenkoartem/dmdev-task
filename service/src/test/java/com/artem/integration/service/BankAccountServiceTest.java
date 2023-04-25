package com.artem.integration.service;

import com.artem.mapper.BankAccountMapper;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.BankAccountReadDto;
import com.artem.model.dto.BankAccountUpdateDto;
import com.artem.model.entity.Account;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.User;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.Role;
import com.artem.model.type.UserStatus;
import com.artem.repository.BankAccountRepository;
import com.artem.service.BankAccountService;
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
import static com.artem.util.ConstantUtil.ALL_3_BANK_ACCOUNTS;
import static com.artem.util.ConstantUtil.BANK_ACCOUNT_1;
import static com.artem.util.ConstantUtil.USER_1;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @InjectMocks
    private BankAccountService bankAccountService;

    @Test
    void checkFindAll() {
        var bankAccountList = List.of(getBankAccount(), getBankAccount(), getBankAccount());
        var expectedResponse = List.of(getBankAccountReadDto(), getBankAccountReadDto(), getBankAccountReadDto());
        doReturn(bankAccountList).when(bankAccountRepository).findAll();
        doReturn(getBankAccountReadDto(), getBankAccountReadDto(), getBankAccountReadDto())
                .when(bankAccountMapper).mapFrom(any(BankAccount.class));

        var actualResponse = bankAccountService.findAll();

        assertThat(actualResponse).hasSize(ALL_3_BANK_ACCOUNTS);
        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    void checkFindById() {
        var bankAccount = getBankAccount();
        var expectedResponse = getBankAccountReadDto();
        doReturn(Optional.of(bankAccount)).when(bankAccountRepository).findById(BANK_ACCOUNT_1);
        doReturn(expectedResponse).when(bankAccountMapper).mapFrom(bankAccount);

        var actualResponse = bankAccountService.findById(BANK_ACCOUNT_1);

        assertThat(Optional.of(expectedResponse)).isEqualTo(actualResponse);
    }

    @Test
    void checkCreate() {
        var bankAccountCreateDto = getBankAccountCreateDto();
        var bankAccount = getBankAccount();
        var expectedResponse = getBankAccountReadDto();
        doReturn(bankAccount).when(bankAccountMapper).mapFrom(bankAccountCreateDto);
        doReturn(bankAccount).when(bankAccountRepository).save(bankAccount);
        doReturn(expectedResponse).when(bankAccountMapper).mapFrom(bankAccount);

        var actualResponse = bankAccountService.create(bankAccountCreateDto);

        assertThat(actualResponse.id()).isNotNull();
        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    void checkUpdate() {
        var bankAccount = getBankAccount();
        var bankAccountUpdateDto = getBankAccountUpdateDto();
        var updatedBankAccount = getUpdatedBankAccount();
        var expectedResponse = getUpdatedBankAccountReadDto();
        doReturn(Optional.of(bankAccount)).when(bankAccountRepository).findById(BANK_ACCOUNT_1);
        doReturn(updatedBankAccount).when(bankAccountMapper).mapFrom(bankAccount, bankAccountUpdateDto);
        doReturn(updatedBankAccount).when(bankAccountRepository).saveAndFlush(updatedBankAccount);
        doReturn(expectedResponse).when(bankAccountMapper).mapFrom(updatedBankAccount);

        var actualResponse = bankAccountService.update(BANK_ACCOUNT_1, bankAccountUpdateDto);

        assertThat(Optional.of(expectedResponse)).isEqualTo(actualResponse);
    }

    @Test
    void checkDelete() {
        var bankAccount = getBankAccount();
        doReturn(Optional.of(bankAccount)).when(bankAccountRepository).findById(BANK_ACCOUNT_1);

        var expectedResult = bankAccountService.delete(BANK_ACCOUNT_1);

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

    private BankAccountCreateDto getBankAccountCreateDto() {
        return BankAccountCreateDto.builder()
                .accountId(ACCOUNT_1)
                .number("123456")
                .type(AccountType.CHECKING_ACCOUNT)
                .status(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(200).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(100).setScale(2, RoundingMode.CEILING))
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

    private BankAccountReadDto getBankAccountReadDto() {
        return BankAccountReadDto.builder()
                .id(BANK_ACCOUNT_1)
                .accountId(ACCOUNT_1)
                .number("123456")
                .type(AccountType.CHECKING_ACCOUNT)
                .status(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(200).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(100).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private BankAccountUpdateDto getBankAccountUpdateDto() {
        return BankAccountUpdateDto.builder()
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.BLOCKED)
                .availableBalance(BigDecimal.valueOf(300).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(200).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private BankAccount getUpdatedBankAccount() {
        return BankAccount.builder()
                .id(BANK_ACCOUNT_1)
                .number("123456")
                .type(AccountType.SAVINGS_ACCOUNT)
                .status(AccountStatus.BLOCKED)
                .availableBalance(BigDecimal.valueOf(300).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(200).setScale(2, RoundingMode.CEILING))
                .account(getAccount())
                .build();
    }

    private BankAccountReadDto getUpdatedBankAccountReadDto() {
        return BankAccountReadDto.builder()
                .id(BANK_ACCOUNT_1)
                .accountId(ACCOUNT_1)
                .number("123456")
                .type(AccountType.SAVINGS_ACCOUNT)
                .status(AccountStatus.BLOCKED)
                .availableBalance(BigDecimal.valueOf(300).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(200).setScale(2, RoundingMode.CEILING))
                .build();
    }
}
