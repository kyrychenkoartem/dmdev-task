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

import static com.artem.util.ConstantUtil.ACCOUNT_ID_ONE;
import static com.artem.util.ConstantUtil.ALL_ACCOUNTS;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountRepositoryTest extends RepositoryTestBase {

    private final AccountRepository accountRepository = context.getBean(AccountRepository.class);
    private final UserRepository userRepository = context.getBean(UserRepository.class);
    private final AccountMapper accountMapper = context.getBean(AccountMapper.class);
    private final UserMapper userMapper = context.getBean(UserMapper.class);

    @Test
    void checkAccountSave() {
        Account expectedAccount = saveAccount();
        session.clear();

        assertThat(accountRepository.findById(expectedAccount.getId()).get().getId()).isNotNull();
    }

    @Test
    void checkAccountDelete() {
        var actualAccount = accountRepository.findById(ACCOUNT_ID_ONE);

        accountRepository.delete(actualAccount.get());

        assertThat(accountRepository.findById(actualAccount.get().getId())).isEmpty();
    }

    @Test
    void checkAccountUpdate() {
        var maybeAccount = accountRepository.findById(ACCOUNT_ID_ONE);
        var updateDto = AccountUpdateDto.builder()
                .status(AccountStatus.BLOCKED)
                .build();
        var expectedAccount = accountMapper.mapFrom(maybeAccount.get(), updateDto);

        accountRepository.update(expectedAccount);
        session.clear();
        var actualAccount = accountRepository.findById(ACCOUNT_ID_ONE).get();

        assertThat(actualAccount.getStatus()).isEqualTo(AccountStatus.BLOCKED);
        assertThat(actualAccount.getUpdatedBy()).isEqualTo(expectedAccount.getUpdatedBy());
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

        assertThat(accountList.size()).isEqualTo(ALL_ACCOUNTS);
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
                .userId(actualUser.getId())
                .build();
        var account = accountMapper.mapFrom(accountCreateDto);
        var expectedAccount = accountRepository.save(account);
        return expectedAccount;
    }
}
