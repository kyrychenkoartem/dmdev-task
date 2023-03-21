package com.artem.dao;

import com.artem.mapper.BankAccountMapper;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.BankAccountUpdateDto;
import com.artem.model.entity.BankAccount;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ACCOUNT_ID_ONE;
import static com.artem.util.ConstantUtil.ALL_BANK_ACCOUNTS;
import static com.artem.util.ConstantUtil.BANK_ACCOUNT_ID_ONE;
import static org.assertj.core.api.Assertions.assertThat;

class BankAccountRepositoryTest extends RepositoryTestBase {

    private final BankAccountRepository bankAccountRepository = context.getBean(BankAccountRepository.class);
    private final BankAccountMapper accountMapper = context.getBean(BankAccountMapper.class);

    @Test
    void checkBankAccountSave() {
        BankAccount expectedBankAccount = saveBankAccount();
        session.clear();

        assertThat(bankAccountRepository.findById(expectedBankAccount.getId()).get().getId()).isNotNull();
    }

    @Test
    void checkBankAccountDelete() {
        var actualBankAccount = bankAccountRepository.findById(BANK_ACCOUNT_ID_ONE);

        bankAccountRepository.delete(actualBankAccount.get());

        assertThat(bankAccountRepository.findById(actualBankAccount.get().getId())).isEmpty();
    }

    @Test
    void checkBankAccountUpdate() {
        var maybeBankAccount = bankAccountRepository.findById(BANK_ACCOUNT_ID_ONE);
        var updateDto = getBankAccountUpdateDto();
        var expectedBankAccount = accountMapper.mapFrom(maybeBankAccount.get(), updateDto);

        bankAccountRepository.update(expectedBankAccount);
        session.clear();
        var actualBankAccount = bankAccountRepository.findById(BANK_ACCOUNT_ID_ONE).get();

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

    private BankAccountCreateDto getBankAccountCreateDto() {
        return BankAccountCreateDto.builder()
                .accountId(ACCOUNT_ID_ONE)
                .number("234554356765646586")
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(500).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(600).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private BankAccount saveBankAccount() {
        var accountCreateDto = getBankAccountCreateDto();
        var bankAccount = accountMapper.mapFrom(accountCreateDto);
        var expectedBankAccount = bankAccountRepository.save(bankAccount);
        return expectedBankAccount;
    }
}
