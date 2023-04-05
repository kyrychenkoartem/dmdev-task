package com.artem.integration.repository;

import com.artem.repository.AccountRepository;
import com.artem.repository.BankAccountRepository;
import com.artem.repository.FundTransferRepository;
import com.artem.repository.TransactionRepository;
import com.artem.repository.UserRepository;
import com.artem.mapper.AccountMapper;
import com.artem.mapper.BankAccountMapper;
import com.artem.mapper.FundTransferMapper;
import com.artem.mapper.TransactionMapper;
import com.artem.mapper.UserMapper;
import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.FundTransferCreateDto;
import com.artem.model.dto.FundTransferUpdateDto;
import com.artem.model.dto.TransactionCreateDto;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.Transaction;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.Role;
import com.artem.model.type.TransactionStatus;
import com.artem.model.type.TransactionType;
import com.artem.util.DateTimeGenerator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_FUND_TRANSFERS;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class FundTransferRepositoryTest extends RepositoryTestBase {

    private final FundTransferRepository fundTransferRepository;
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final BankAccountMapper bankAccountMapper;
    private final UserMapper userMapper;
    private final FundTransferMapper fundTransferMapper;
    private final TransactionMapper transactionMapper;
    private final EntityManager session;

    @Test
    void checkAccountSave() {
        var fromBankAccount = saveFromBankAccount();
        var toBankAccount = saveToBankAccount();
        session.clear();
        var fromAccount = bankAccountRepository.findById(fromBankAccount.getId());
        var toAccount = bankAccountRepository.findById(toBankAccount.getId());
        var transaction = saveTransaction(toAccount.get(), fromAccount.get());
        var fundTransferCreateDto = getFundTransferCreateDto(fromAccount.get(), toAccount.get(), transaction);
        var fundTransfer = fundTransferMapper.mapFrom(fundTransferCreateDto);
        var expectedFundTransfer = fundTransferRepository.save(fundTransfer);
        session.clear();

        assertThat(fundTransferRepository.findById(expectedFundTransfer.getId()).get().getId()).isNotNull();
    }

    @Test
    void checkAccountDelete() {
        var fromBankAccount = saveFromBankAccount();
        var toBankAccount = saveToBankAccount();
        session.clear();
        var fromAccount = bankAccountRepository.findById(fromBankAccount.getId());
        var toAccount = bankAccountRepository.findById(toBankAccount.getId());
        var transaction = saveTransaction(toAccount.get(), fromAccount.get());
        var fundTransferCreateDto = getFundTransferCreateDto(fromAccount.get(), toAccount.get(), transaction);
        var fundTransfer = fundTransferMapper.mapFrom(fundTransferCreateDto);
        var expectedFundTransfer = fundTransferRepository.save(fundTransfer);
        session.clear();
        var actualFundTransfer = fundTransferRepository.findById(expectedFundTransfer.getId());

        fundTransferRepository.delete(actualFundTransfer.get());

        assertThat(fundTransferRepository.findById(actualFundTransfer.get().getId())).isEmpty();
    }

    @Test
    void checkAccountUpdate() {
        var fromBankAccount = saveFromBankAccount();
        var toBankAccount = saveToBankAccount();
        session.clear();
        var fromAccount = bankAccountRepository.findById(fromBankAccount.getId());
        var toAccount = bankAccountRepository.findById(toBankAccount.getId());
        var transaction = saveTransaction(toAccount.get(), fromAccount.get());
        var fundTransferCreateDto = getFundTransferCreateDto(fromAccount.get(), toAccount.get(), transaction);
        var fundTransferToSave = fundTransferMapper.mapFrom(fundTransferCreateDto);
        var fundTransfer = fundTransferRepository.save(fundTransferToSave);
        session.clear();
        var maybeFundTransfer = fundTransferRepository.findById(fundTransfer.getId());
        var transferUpdateDto = getTransferUpdateDto();
        var expectedFundTransfer = fundTransferMapper.mapFrom(maybeFundTransfer.get(), transferUpdateDto);

        fundTransferRepository.saveAndFlush(expectedFundTransfer);
        session.clear();
        var actualFundTransfer = fundTransferRepository.findById(expectedFundTransfer.getId()).get();

        assertThat(actualFundTransfer.getAmount()).isEqualTo(BigDecimal.valueOf(70).setScale(2, RoundingMode.CEILING));
        assertThat(actualFundTransfer.getStatus()).isEqualTo(TransactionStatus.SUCCESS);
    }

    @Test
    void checkAccountFindById() {
        var fromBankAccount = saveFromBankAccount();
        var toBankAccount = saveToBankAccount();
        session.clear();
        var fromAccount = bankAccountRepository.findById(fromBankAccount.getId());
        var toAccount = bankAccountRepository.findById(toBankAccount.getId());
        var transaction = saveTransaction(toAccount.get(), fromAccount.get());
        var fundTransferCreateDto = getFundTransferCreateDto(fromAccount.get(), toAccount.get(), transaction);
        var fundTransfer = fundTransferMapper.mapFrom(fundTransferCreateDto);
        var expectedFundTransfer = fundTransferRepository.save(fundTransfer);
        session.clear();

        var actualUtilityAccount = fundTransferRepository.findById(expectedFundTransfer.getId());

        assertThat(expectedFundTransfer).isEqualTo(actualUtilityAccount.get());
    }

    @Test
    void checkFindAllAccounts() {
        var fundTransferList = fundTransferRepository.findAll();

        assertThat(fundTransferList.size()).isEqualTo(ALL_FUND_TRANSFERS);
    }

    private Transaction saveTransaction(BankAccount toAccount, BankAccount fromAccount) {
        var transactionCreateDto = getTransactionCreateDto(toAccount, fromAccount);
        var transaction = transactionMapper.mapFrom(transactionCreateDto);
        return transactionRepository.save(transaction);
    }

    private String getTransactionId() {
        return UUID.randomUUID().toString();
    }

    private UserCreateDto getUserCreateDto1() {
        return UserCreateDto.builder()
                .firstname("Test")
                .lastname("Test")
                .email("test@gmail.com")
                .password("testPassword")
                .birthDate(LocalDate.of(2023, 3, 11))
                .role(Role.USER)
                .build();
    }

    private UserCreateDto getUserCreateDto2() {
        return UserCreateDto.builder()
                .firstname("Test1")
                .lastname("Test1")
                .email("test1@gmail.com")
                .password("test1Password")
                .birthDate(LocalDate.of(2023, 3, 11))
                .role(Role.USER)
                .build();
    }

    private BankAccountCreateDto getToBankAccountCreateDto() {
        var userCreateDto = getUserCreateDto1();
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

    private BankAccountCreateDto getFromBankAccountCreateDto() {
        var userCreateDto = getUserCreateDto2();
        var user = userMapper.mapFrom(userCreateDto);
        var actualUser = userRepository.save(user);
        var accountCreateDto = AccountCreateDto.builder()
                .userId(actualUser.getId())
                .build();
        var account = accountMapper.mapFrom(accountCreateDto);
        var expectedAccount = accountRepository.save(account);
        return BankAccountCreateDto.builder()
                .accountId(expectedAccount.getId())
                .number("234554356765646587")
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(500).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(600).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private BankAccount saveToBankAccount() {
        var accountCreateDto = getToBankAccountCreateDto();
        var bankAccount = bankAccountMapper.mapFrom(accountCreateDto);
        return bankAccountRepository.save(bankAccount);
    }

    private BankAccount saveFromBankAccount() {
        var accountCreateDto = getFromBankAccountCreateDto();
        var bankAccount = bankAccountMapper.mapFrom(accountCreateDto);
        return bankAccountRepository.save(bankAccount);
    }

    private TransactionCreateDto getTransactionCreateDto(BankAccount toAccount, BankAccount fromAccount) {
        return TransactionCreateDto.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .type(TransactionType.DEPOSIT)
                .referenceNumber(toAccount.getNumber())
                .transactionId(getTransactionId())
                .time(DateTimeGenerator.getRandomDateTime())
                .bankAccountId(fromAccount.getId())
                .build();
    }

    private FundTransferCreateDto getFundTransferCreateDto(BankAccount fromAccount, BankAccount toAccount, Transaction transaction) {
        return FundTransferCreateDto.builder()
                .fromAccount(fromAccount.getNumber())
                .toAccount(toAccount.getNumber())
                .amount(transaction.getAmount())
                .status(TransactionStatus.PROCESSING)
                .transactionId(transaction.getTransactionId())
                .build();
    }

    private FundTransferUpdateDto getTransferUpdateDto() {
        return FundTransferUpdateDto.builder()
                .amount(BigDecimal.valueOf(70).setScale(2, RoundingMode.CEILING))
                .status(TransactionStatus.SUCCESS)
                .build();
    }
}
