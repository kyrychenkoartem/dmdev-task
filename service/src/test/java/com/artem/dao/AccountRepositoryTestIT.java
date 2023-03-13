package com.artem.dao;

import com.artem.mapper.AccountMapper;
import com.artem.mapper.UserMapper;
import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.AccountUpdateDto;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.entity.Account;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.Role;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_USERS;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountRepositoryTestIT extends RepositoryTestBase {

    private final AccountRepository accountRepository = new AccountRepository(session);
    private final UserRepository userRepository = new UserRepository(session);
    private final AccountMapper accountMapper = new AccountMapper();
    private final UserMapper userMapper = new UserMapper();

    @Test
    void checkAccountSave() {
        Account expectedAccount = saveAccount();

        assertThat(expectedAccount.getId()).isNotNull();
    }

    @Test
    void checkAccountDelete() {
        var actualAccount = accountRepository.findById(1L);

        accountRepository.delete(actualAccount.get());

        assertThat(accountRepository.findById(actualAccount.get().getId())).isEmpty();
    }

    @Test
    void checkAccountUpdate() {
        var actualAccount = accountRepository.findById(1L);
        var updateDto = AccountUpdateDto.builder()
                .status(AccountStatus.BLOCKED)
                .build();
        var expectedAccount = accountMapper.mapFrom(actualAccount.get(), updateDto);

        accountRepository.update(expectedAccount);
        session.clear();

        assertThat(accountRepository.findById(1L).get().getStatus()).isEqualTo(AccountStatus.BLOCKED);
        assertThat(accountRepository.findById(1L).get().getUpdatedBy()).isEqualTo(expectedAccount.getUpdatedBy());
    }

    @Test
    void checkAccountFindById() {
        Account expectedAccount = saveAccount();
        session.clear();

        var actualAccount = accountRepository.findById(expectedAccount.getId());

        assertThat(expectedAccount).isEqualTo(actualAccount.get());
    }

    @Test
    void checkFindAllAccounts() {
        var accountList = accountRepository.findAll();

        assertThat(accountList.size()).isEqualTo(ALL_USERS);
    }

    private UserCreateDto getAccountCreateDto() {
        return UserCreateDto.builder()
                .firstname("Test")
                .lastname("Test")
                .email("test@gmail.com")
                .password("testPassword")
                .birthDate(LocalDate.of(2023, 3, 11))
                .role(Role.USER)
                .build();
    }

    private Account saveAccount() {
        var userCreateDto = getAccountCreateDto();
        var user = userMapper.mapFrom(userCreateDto);
        var actualUser = userRepository.save(user);
        var accountCreateDto = AccountCreateDto.builder()
                .user(actualUser)
                .build();
        var account = accountMapper.mapFrom(accountCreateDto);
        var expectedAccount = accountRepository.save(account);
        return expectedAccount;
    }
}
