package com.artem.dao;

import com.artem.mapper.BankAccountMapper;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.BankAccountUpdateDto;
import com.artem.model.entity.Account;
import com.artem.model.entity.BankAccount;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_BANK_ACCOUNTS;
import static org.assertj.core.api.Assertions.assertThat;

public class BankAccountRepositoryTestIT extends RepositoryTestBase {

    private final BankAccountRepository bankAccountRepository = new BankAccountRepository(session);
    private final AccountRepository accountRepository = new AccountRepository(session);
    private final BankAccountMapper accountMapper = new BankAccountMapper();

    @Test
    void checkBankAccountSave() {
        BankAccount expectedBankAccount = saveBankAccount();

        assertThat(expectedBankAccount.getId()).isNotNull();
    }

    @Test
    void checkBankAccountDelete() {
        var actualBankAccount = bankAccountRepository.findById(1L);

        bankAccountRepository.delete(actualBankAccount.get());

        assertThat(bankAccountRepository.findById(actualBankAccount.get().getId())).isEmpty();
    }

    @Test
    void checkBankAccountUpdate() {
        var actualBankAccount = bankAccountRepository.findById(1L);
        var updateDto = getBankAccountUpdateDto();
        var expectedBankAccount = accountMapper.mapFrom(actualBankAccount.get(), updateDto);

        bankAccountRepository.update(expectedBankAccount);
        session.clear();

        assertThat(bankAccountRepository.findById(1L).get().getType()).isEqualTo(AccountType.LOAN_ACCOUNT);
        assertThat(bankAccountRepository.findById(1L).get().getStatus()).isEqualTo(AccountStatus.BLOCKED);
        assertThat(bankAccountRepository.findById(1L).get().getAvailableBalance())
                .isEqualTo(BigDecimal.valueOf(500).setScale(2, RoundingMode.CEILING));
        assertThat(bankAccountRepository.findById(1L).get().getActualBalance())
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

    private BankAccountCreateDto getBankAccountCreateDto(Optional<Account> account) {
        return BankAccountCreateDto.builder()
                .account(account.get())
                .number("234554356765646586")
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(500).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(600).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private BankAccount saveBankAccount() {
        var account = accountRepository.findById(1L);
        var accountCreateDto = getBankAccountCreateDto(account);
        var bankAccount = accountMapper.mapFrom(accountCreateDto);
        var expectedBankAccount = bankAccountRepository.save(bankAccount);
        return expectedBankAccount;
    }
}
