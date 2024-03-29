package com.artem.integration.repository;

import com.artem.repository.AccountRepository;
import com.artem.repository.UserRepository;
import com.artem.mapper.AccountMapper;
import com.artem.mapper.UserMapper;
import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.AccountUpdateDto;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.entity.Account;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.Role;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_ACCOUNTS;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class AccountRepositoryTest extends RepositoryTestBase {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final UserMapper userMapper;
    private final EntityManager session;

    @Test
    void checkAccountSave() {
        var expectedAccount = saveAccount();
        session.clear();

        assertThat(accountRepository.findById(expectedAccount.getId()).get().getId()).isNotNull();
    }

    @Test
    void checkAccountDelete() {
        var expectedAccount = saveAccount();
        session.clear();
        var actualAccount = accountRepository.findById(expectedAccount.getId());

        accountRepository.delete(actualAccount.get());

        assertThat(accountRepository.findById(actualAccount.get().getId())).isEmpty();
    }

    @Test
    void checkAccountUpdate() {
        var account = saveAccount();
        session.clear();
        var maybeAccount = accountRepository.findById(account.getId());
        var updateDto = AccountUpdateDto.builder()
                .status(AccountStatus.BLOCKED)
                .build();
        var expectedAccount = accountMapper.mapFrom(maybeAccount.get(), updateDto);

        accountRepository.saveAndFlush(expectedAccount);
        session.clear();
        var actualAccount = accountRepository.findById(expectedAccount.getId()).get();

        assertThat(actualAccount.getStatus()).isEqualTo(AccountStatus.BLOCKED);
        assertThat(actualAccount.getUpdatedBy()).isEqualTo(expectedAccount.getUpdatedBy());
    }

    @Test
    void checkAccountFindById() {
        var expectedAccount = saveAccount();
        session.clear();

        var actualAccount = accountRepository.findById(expectedAccount.getId());

        assertThat(expectedAccount).isEqualTo(actualAccount.get());
    }

    @Test
    void checkFindAllAccounts() {
        var accountList = accountRepository.findAll();

        assertThat(accountList.size()).isEqualTo(ALL_ACCOUNTS);
    }

    private UserCreateDto getUserCreateDto() {
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
        var userCreateDto = getUserCreateDto();
        var user = userMapper.mapFrom(userCreateDto);
        var actualUser = userRepository.save(user);
        var accountCreateDto = AccountCreateDto.builder()
                .status(AccountStatus.ACTIVE)
                .userId(actualUser.getId())
                .build();
        var account = accountMapper.mapFrom(accountCreateDto);
        return accountRepository.save(account);
    }
}
