package com.artem.integration.repository;

import com.artem.repository.AccountRepository;
import com.artem.repository.BankAccountRepository;
import com.artem.repository.TransactionRepository;
import com.artem.repository.UserRepository;
import com.artem.mapper.AccountMapper;
import com.artem.mapper.BankAccountMapper;
import com.artem.mapper.TransactionMapper;
import com.artem.mapper.UserMapper;
import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.TransactionCreateDto;
import com.artem.model.dto.TransactionFilter;
import com.artem.model.dto.TransactionUpdateDto;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.Transaction;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import com.artem.model.type.Role;
import com.artem.model.type.TransactionType;
import com.artem.util.DateTimeGenerator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_TRANSACTIONS;
import static com.artem.util.ConstantUtil.TRANSACTION_COUNT_FOUR_EXPECTED;
import static com.artem.util.ConstantUtil.TRANSACTION_COUNT_ONE_EXPECTED;
import static com.artem.util.ConstantUtil.UTILITY_ACCOUNT_KOODO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class TransactionRepositoryTest extends RepositoryTestBase {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final BankAccountMapper bankAccountMapper;
    private final UserMapper userMapper;
    private final TransactionMapper transactionMapper;
    private final EntityManager session;

    @Test
    void checkAccountSave() {
        var expectedTransaction = saveTransaction();
        session.clear();

        assertThat(transactionRepository.findById(expectedTransaction.getId()).get().getId()).isNotNull();
    }

    @Test
    void checkAccountDelete() {
        var transaction = saveTransaction();
        session.clear();
        var actualTransaction = transactionRepository.findById(transaction.getId());

        transactionRepository.delete(actualTransaction.get());

        assertThat(transactionRepository.findById(actualTransaction.get().getId())).isEmpty();
    }

    @Test
    void checkAccountUpdate() {
        var transaction = saveTransaction();
        session.clear();
        var maybeTransaction = transactionRepository.findById(transaction.getId());
        var updateDto = getTransactionUpdateDto();
        var expectedTransaction = transactionMapper.mapFrom(maybeTransaction.get(), updateDto);

        transactionRepository.saveAndFlush(expectedTransaction);
        session.clear();
        var actualTransaction = transactionRepository.findById(expectedTransaction.getId()).get();

        assertThat(actualTransaction.getAmount()).isEqualTo(BigDecimal.valueOf(60).setScale(2, RoundingMode.CEILING));
        assertThat(actualTransaction.getTransactionType()).isEqualTo(TransactionType.REFUND);
    }

    @Test
    void checkAccountFindById() {
        var expectedTransaction = saveTransaction();
        session.clear();

        var actualTransaction = transactionRepository.findById(expectedTransaction.getId());

        assertThat(expectedTransaction).isEqualTo(actualTransaction.get());
    }

    @Test
    void checkFindAllAccounts() {
        var transactionList = transactionRepository.findAll();

        assertThat(transactionList.size()).isEqualTo(ALL_TRANSACTIONS);
    }

    @Test
    void checkGetTransactionByUser() {
        var expectedTransaction = saveTransaction();
        session.clear();
        var transactions = transactionRepository
                .getTransactionsByUser(expectedTransaction.getBankAccount().getAccount().getUser().getId());

        var userEmail = transactions.stream()
                .map(it -> it.getBankAccount().getAccount().getUser().getEmail())
                .findFirst();

        assertThat(transactions).hasSize(TRANSACTION_COUNT_ONE_EXPECTED);
        assertThat(userEmail.get()).isEqualTo("test@gmail.com");
    }

    @Test
    void checkGetTransactionsByBankAccount() {
        var expectedTransaction = saveTransaction();
        session.clear();
        var transactions = transactionRepository
                .getTransactionsByBankAccount(expectedTransaction.getBankAccount().getId());

        var bankAccountNumber = transactions.stream()
                .map(it -> it.getBankAccount().getNumber())
                .findFirst();

        assertThat(transactions).hasSize(TRANSACTION_COUNT_ONE_EXPECTED);
        assertThat(bankAccountNumber.get()).isEqualTo("234554356765646586");
    }

    @Test
    void checkGetTransactionByUtilityAccountName() {
        var transactions = transactionRepository.getTransactionByUtilityAccountName(UTILITY_ACCOUNT_KOODO);

        assertThat(transactions).hasSize(TRANSACTION_COUNT_FOUR_EXPECTED);
    }

    @Test
    void checkGetSumTransactionsPaymentByBankAccount() {
        var expectedTransaction = saveTransaction();
        session.clear();
        var sumTransactionsPayment = transactionRepository
                .getSumTransactionsPaymentByBankAccount(expectedTransaction.getBankAccount().getId());

        assertThat(sumTransactionsPayment).isEqualTo(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING));
    }

    @Test
    void checkGetLimitedTransactionsByBankAccountOrderedByTime() {
        var expectedTransaction = saveTransaction();
        session.clear();
        var transactions = transactionRepository
                .getLimitedTransactionsByBankAccountOrderedByTimeAsc(expectedTransaction.getBankAccount().getId(), 10);

        assertThat(transactions).hasSize(TRANSACTION_COUNT_ONE_EXPECTED);

    }

    @Test
    void checkGetTransactionsByUserOrderedByTimeDesc() {
        var expectedTransaction = saveTransaction();
        session.clear();
        var transactions = transactionRepository
                .getTransactionsByUserOrderedByTimeDesc(expectedTransaction.getBankAccount().getAccount().getUser().getId());

        var times = transactions.stream()
                .map(Transaction::getTime)
                .toList();

        assertThat(transactions).hasSize(TRANSACTION_COUNT_ONE_EXPECTED);
        assertThat(times.get(0)).isAfter(LocalDateTime.of(1970, 1, 1, 0, 0));
    }

    @Test
    void checkGetTransactionsByAccountByLastDateAndReferenceNumber() {
        var expectedTransaction = saveTransaction();
        session.clear();
        var filter = TransactionFilter.builder()
                .referenceNumber("234554356765646586")
                .time(LocalDateTime.of(1970, 1, 1, 0, 0))
                .build();
        var transactions = transactionRepository
                .getTransactionsByUserByLastDate(expectedTransaction.getBankAccount().getAccount().getUser().getId(), filter);

        var referenceNumbers = transactions.stream()
                .map(Transaction::getReferenceNumber)
                .toList();
        var times = transactions.stream()
                .map(Transaction::getTime)
                .toList();

        assertTrue(referenceNumbers.stream().anyMatch(it -> it.equals("234554356765646586")));
        assertTrue(times.stream().allMatch(it -> LocalDateTime.of(1970, 1, 1, 0, 0).isBefore(it)));
    }

    private String getTransactionId() {
        return UUID.randomUUID().toString();
    }

    private TransactionCreateDto getTransactionCreateDto(BankAccount bankAccount) {
        return TransactionCreateDto.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .type(TransactionType.DEPOSIT)
                .referenceNumber(bankAccount.getNumber())
                .transactionId(getTransactionId())
                .bankAccountId(bankAccount.getId())
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
                .status(AccountStatus.ACTIVE)
                .userId(actualUser.getId())
                .build();
        var account = accountMapper.mapFrom(accountCreateDto);
        var expectedAccount = accountRepository.save(account);
        return BankAccountCreateDto.builder()
                .accountId(expectedAccount.getId())
                .number("234554356765646586")
                .type(AccountType.SAVINGS_ACCOUNT)
                .status(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(500).setScale(2, RoundingMode.CEILING))
                .actualBalance(BigDecimal.valueOf(600).setScale(2, RoundingMode.CEILING))
                .build();
    }

    private BankAccount saveBankAccount() {
        var accountCreateDto = getBankAccountCreateDto();
        var bankAccount = bankAccountMapper.mapFrom(accountCreateDto);
        return bankAccountRepository.save(bankAccount);
    }

    private Transaction saveTransaction() {
        var bankAccount = saveBankAccount();
        var transactionCreateDto = getTransactionCreateDto(bankAccount);
        var transaction = transactionMapper.mapFrom(transactionCreateDto);
        return transactionRepository.save(transaction);
    }

    private TransactionUpdateDto getTransactionUpdateDto() {
        return TransactionUpdateDto.builder()
                .amount(BigDecimal.valueOf(60).setScale(2, RoundingMode.CEILING))
                .type(TransactionType.REFUND)
                .build();
    }
}
