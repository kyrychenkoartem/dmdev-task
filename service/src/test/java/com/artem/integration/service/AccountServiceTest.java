package com.artem.integration.service;


import com.artem.mapper.AccountMapper;
import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.AccountReadDto;
import com.artem.model.dto.AccountUpdateDto;
import com.artem.model.entity.Account;
import com.artem.model.entity.User;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.Role;
import com.artem.model.type.UserStatus;
import com.artem.repository.AccountRepository;
import com.artem.service.AccountService;
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
import static com.artem.util.ConstantUtil.ALL_3_ACCOUNTS;
import static com.artem.util.ConstantUtil.USER_1;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    @Test
    void checkFindAll() {
        var accountList = List.of(getAccount(), getAccount(), getAccount());
        var expectedResponse = List.of(getAccountReadDto(), getAccountReadDto(), getAccountReadDto());
        doReturn(accountList).when(accountRepository).findAll();
        doReturn(getAccountReadDto(), getAccountReadDto(), getAccountReadDto()).when(accountMapper).mapFrom(any(Account.class));

        var actualResponse = accountService.findAll();

        assertThat(actualResponse).hasSize(ALL_3_ACCOUNTS);
        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    void checkFindById() {
        var account = getAccount();
        var expectedResponse = getAccountReadDto();
        doReturn(Optional.of(account)).when(accountRepository).findById(ACCOUNT_1);
        doReturn(expectedResponse).when(accountMapper).mapFrom(account);

        var actualResponse = accountService.findById(ACCOUNT_1);

        assertThat(Optional.of(expectedResponse)).isEqualTo(actualResponse);
    }

    @Test
    void checkCreate() {
        var accountCreateDto = getAccountCreateDto();
        var account = getAccount();
        var expectedResponse = getAccountReadDto();
        doReturn(account).when(accountMapper).mapFrom(accountCreateDto);
        doReturn(account).when(accountRepository).save(account);
        doReturn(expectedResponse).when(accountMapper).mapFrom(account);

        var actualResponse = accountService.create(accountCreateDto);

        assertThat(actualResponse.id()).isNotNull();
        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    void checkUpdate() {
        var account = getAccount();
        var accountUpdateDto = getAccountUpdateDto();
        var expectedAccount = getUpdatedAccount();
        var expectedResponse = getUpdatedAccountReadDto();
        doReturn(Optional.of(account)).when(accountRepository).findById(ACCOUNT_1);
        doReturn(expectedAccount).when(accountMapper).mapFrom(account, accountUpdateDto);
        doReturn(expectedAccount).when(accountRepository).saveAndFlush(expectedAccount);
        doReturn(expectedResponse).when(accountMapper).mapFrom(expectedAccount);

        var actualResponse = accountService.update(ACCOUNT_1, accountUpdateDto);

        assertThat(Optional.of(expectedResponse)).isEqualTo(actualResponse);
    }

    @Test
    void checkDelete() {
        var account = getAccount();
        doReturn(Optional.of(account)).when(accountRepository).findById(ACCOUNT_1);

        var expectedResult = accountService.delete(ACCOUNT_1);

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

    private AccountCreateDto getAccountCreateDto() {
        return AccountCreateDto.builder()
                .userId(USER_1)
                .status(AccountStatus.ACTIVE)
                .build();
    }

    private AccountUpdateDto getAccountUpdateDto() {
        return AccountUpdateDto.builder()
                .status(AccountStatus.BLOCKED)
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

    private Account getUpdatedAccount() {
        return Account.builder()
                .id(ACCOUNT_1)
                .user(getUser())
                .status(AccountStatus.BLOCKED)
                .createdAt(LocalDateTime.of(2000, 1, 1, 0, 0).truncatedTo(DAYS))
                .createdBy(getUser().getEmail())
                .updatedAt(LocalDateTime.of(2001, 1, 1, 0, 0).truncatedTo(DAYS))
                .updatedBy(getUser().getEmail())
                .build();
    }

    private AccountReadDto getAccountReadDto() {
        return AccountReadDto.builder()
                .id(ACCOUNT_1)
                .userId(USER_1)
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2000, 1, 1, 0, 0).truncatedTo(DAYS))
                .createdBy(getUser().getEmail())
                .build();
    }

    private AccountReadDto getUpdatedAccountReadDto() {
        return AccountReadDto.builder()
                .id(ACCOUNT_1)
                .userId(USER_1)
                .status(AccountStatus.BLOCKED)
                .createdAt(LocalDateTime.of(2000, 1, 1, 0, 0).truncatedTo(DAYS))
                .createdBy(getUser().getEmail())
                .updatedAt(LocalDateTime.of(2001, 1, 1, 0, 0).truncatedTo(DAYS))
                .updatedBy(getUser().getEmail())
                .build();
    }
}
