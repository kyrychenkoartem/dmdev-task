package com.artem.integration.dao;

import com.artem.dao.AccountRepository;
import com.artem.dao.BankAccountRepository;
import com.artem.dao.UserRepository;
import com.artem.mapper.AccountMapper;
import com.artem.mapper.BankAccountMapper;
import com.artem.mapper.UserMapper;
import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.BankAccountUpdateDto;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.entity.BankAccount;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.Role;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_BANK_ACCOUNTS;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class BankAccountRepositoryTest extends RepositoryTestBase {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final BankAccountMapper bankAccountMapper;
    private final UserMapper userMapper;
    private final EntityManager session;

    @Test
    void checkBankAccountSave() {
        var expectedBankAccount = saveBankAccount();
        session.clear();

        assertThat(bankAccountRepository.findById(expectedBankAccount.getId()).get().getId()).isNotNull();
    }

    @Test
    void checkBankAccountDelete() {
        var expectedBankAccount = saveBankAccount();
        session.clear();
        var actualBankAccount = bankAccountRepository.findById(expectedBankAccount.getId());

        bankAccountRepository.delete(actualBankAccount.get());

        assertThat(bankAccountRepository.findById(actualBankAccount.get().getId())).isEmpty();
    }

    @Test
    void checkBankAccountUpdate() {
        var bankAccount = saveBankAccount();
        session.clear();
        var maybeBankAccount = bankAccountRepository.findById(bankAccount.getId());
        var updateDto = getBankAccountUpdateDto();
        var expectedBankAccount = bankAccountMapper.mapFrom(maybeBankAccount.get(), updateDto);

        bankAccountRepository.saveAndFlush(expectedBankAccount);
        session.clear();
        var actualBankAccount = bankAccountRepository.findById(expectedBankAccount.getId()).get();

        assertThat(actualBankAccount.getType()).isEqualTo(AccountType.LOAN_ACCOUNT);
        assertThat(actualBankAccount.getStatus()).isEqualTo(AccountStatus.BLOCKED);
        assertThat(actualBankAccount.getAvailableBalance())
                .isEqualTo(BigDecimal.valueOf(500).setScale(2, RoundingMode.CEILING));
        assertThat(actualBankAccount.getActualBalance())
                .isEqualTo(BigDecimal.valueOf(600).setScale(2, RoundingMode.CEILING));
    }

    @Test
    void checkBankAccountFindById() {
        var expectedBankAccount = saveBankAccount();
        session.clear();

        var actualBankAccount = bankAccountRepository.findById(expectedBankAccount.getId());

        assertThat(expectedBankAccount).isEqualTo(actualBankAccount.get());
    }

    @Test
    void checkBankAccountFindAll() {
        var bankAccountList = bankAccountRepository.findAll();

        assertThat(bankAccountList.size()).isEqualTo(ALL_BANK_ACCOUNTS);
    }

    private BankAccountUpdateDto getBankAccountUpdateDto() {
        return BankAccountUpdateDto.builder()
                .accountType(AccountType.LOAN_ACCOUNT)
                .accountStatus(AccountStatus.BLOCKED)
                .availableBalance(BigDecimal.valueOf(500).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(600).setScale(2, RoundingMode.CEILING))
                .build();
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

    private BankAccountCreateDto getBankAccountCreateDto() {
        var userCreateDto = getUserCreateDto();
        var user = userMapper.mapFrom(userCreateDto);
        var actualUser = userRepository.save(user);
        var accountCreateDto = AccountCreateDto.builder()
                .userId(actualUser.getId())
                .build();
        var account = accountMapper.mapFrom(accountCreateDto);
        var expectedAccount = accountRepository.save(account);
        return BankAccountCreateDto.builder()
                .accountId(expectedAccount.getId())
                .number("234554356765646586")
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(500).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(600).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private BankAccount saveBankAccount() {
        var accountCreateDto = getBankAccountCreateDto();
        var bankAccount = bankAccountMapper.mapFrom(accountCreateDto);
        var expectedBankAccount = bankAccountRepository.save(bankAccount);
        return expectedBankAccount;
    }
}
