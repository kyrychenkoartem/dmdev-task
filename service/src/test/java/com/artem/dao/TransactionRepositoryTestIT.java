package com.artem.dao;

import com.artem.mapper.TransactionMapper;
import com.artem.model.dto.TransactionCreateDto;
import com.artem.model.dto.TransactionFilter;
import com.artem.model.dto.TransactionUpdateDto;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.Transaction;
import com.artem.model.type.TransactionType;
import com.artem.util.DateTimeGenerator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_TRANSACTIONS;
import static com.artem.util.ConstantUtil.BANK_ACCOUNT_ID_FIFTEEN;
import static com.artem.util.ConstantUtil.BANK_ACCOUNT_ID_TEN;
import static com.artem.util.ConstantUtil.BANK_ACCOUNT_ID_THREE;
import static com.artem.util.ConstantUtil.USER_ID_FOUR;
import static com.artem.util.ConstantUtil.USER_ID_ONE;
import static com.artem.util.ConstantUtil.USER_ID_TWO;
import static com.artem.util.ConstantUtil.UTILITY_ACCOUNT_KOODO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionRepositoryTestIT extends RepositoryTestBase {

    private final TransactionRepository transactionRepository = new TransactionRepository(session);
    private final BankAccountRepository bankAccountRepository = new BankAccountRepository(session);
    private final TransactionMapper transactionMapper = new TransactionMapper();

    @Test
    void checkAccountSave() {
        var expectedTransaction = saveTransaction();

        assertThat(expectedTransaction.getId()).isNotNull();
    }

    @Test
    void checkAccountDelete() {
        var actualTransaction = transactionRepository.findById(1L);

        transactionRepository.delete(actualTransaction.get());

        assertThat(transactionRepository.findById(actualTransaction.get().getId())).isEmpty();
    }

    @Test
    void checkAccountUpdate() {
        var actualTransaction = transactionRepository.findById(1L);
        var updateDto = getTransactionUpdateDto();
        var expectedTransaction = transactionMapper.mapFrom(actualTransaction.get(), updateDto);

        transactionRepository.update(expectedTransaction);
        session.clear();

        assertThat(transactionRepository.findById(1L).get().getAmount())
                .isEqualTo(BigDecimal.valueOf(60).setScale(2, RoundingMode.CEILING));
        assertThat(transactionRepository.findById(1L).get().getTransactionType())
                .isEqualTo(TransactionType.REFUND);
    }

    @Test
    void checkAccountFindById() {
        var expectedTransaction = saveTransaction();

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
        var transactions = transactionRepository.getTransactionsByUser(USER_ID_ONE);

        var userEmail = transactions.stream()
                .map(it -> it.getBankAccount().getAccount().getUser().getEmail())
                .findFirst();

        assertThat(transactions).hasSize(14);
        assertThat(userEmail.get()).isEqualTo("ivan@gmail.com");
    }

    @Test
    void checkGetTransactionsByBankAccount() {
        var transactions = transactionRepository.getTransactionsByBankAccount(BANK_ACCOUNT_ID_TEN);

        var bankAccountNumber = transactions.stream()
                .map(it -> it.getBankAccount().getNumber())
                .findFirst();

        assertThat(transactions).hasSize(4);
        assertThat(bankAccountNumber.get()).isEqualTo("0123456789");
    }

    @Test
    void checkGetTransactionByUtilityAccountName() {
        var transactions = transactionRepository.getTransactionByUtilityAccountName(UTILITY_ACCOUNT_KOODO);

        assertThat(transactions).hasSize(4);
    }

    @Test
    void checkGetSumTransactionsPaymentByBankAccount() {
        var sumTransactionsPayment = transactionRepository.getSumTransactionsPaymentByBankAccount(BANK_ACCOUNT_ID_FIFTEEN);

        assertThat(sumTransactionsPayment).isEqualTo(BigDecimal.valueOf(150).setScale(2, RoundingMode.CEILING));
    }

    @Test
    void checkGetLimitedTransactionsByBankAccountOrderedByTime() {
        var transactions = transactionRepository.getLimitedTransactionsByBankAccountOrderedByTimeAsc(BANK_ACCOUNT_ID_THREE, 10);

        var times = transactions.stream()
                .map(Transaction::getTime)
                .toList();

        assertThat(transactions).hasSize(3);
        assertThat(times.get(0)).isBefore(times.get(2));
    }

    @Test
    void checkGetTransactionsByUserOrderedByTimeDesc() {
        var transactions = transactionRepository.getTransactionsByUserOrderedByTimeDesc(USER_ID_TWO);

        var times = transactions.stream()
                .map(Transaction::getTime)
                .toList();

        assertThat(transactions).hasSize(9);
        assertThat(times.get(0)).isAfter(times.get(1));
        assertThat(times.get(7)).isAfter(times.get(8));
    }

    @Test
    void checkGetTransactionsByAccountByLastDateAndReferenceNumber() {
        var filter = TransactionFilter.builder()
                .referenceNumber("1134567890")
                .time(LocalDateTime.of(1980, 1, 1, 0, 0))
                .build();
        var transactions = transactionRepository.getTransactionsByUserByLastDate(USER_ID_FOUR, filter);

        var referenceNumbers = transactions.stream()
                .map(Transaction::getReferenceNumber)
                .toList();
        var times = transactions.stream()
                .map(Transaction::getTime)
                .toList();

        assertTrue(referenceNumbers.stream().anyMatch(it -> it.equals("1134567890")));
        assertTrue(times.stream().allMatch(it -> LocalDateTime.of(1980, 1, 1, 0, 0).isBefore(it)));
    }

    private String getTransactionId() {
        return UUID.randomUUID().toString();
    }

    private TransactionCreateDto getTransactionCreateDto(Optional<BankAccount> bankAccount1, Optional<BankAccount> bankAccount2) {
        return TransactionCreateDto.builder()
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING))
                .type(TransactionType.DEPOSIT)
                .referenceNumber(bankAccount2.get().getNumber())
                .transactionId(getTransactionId())
                .time(DateTimeGenerator.getRandomDateTime())
                .bankAccount(bankAccount1.get())
                .build();
    }

    private Transaction saveTransaction() {
        var bankAccount1 = bankAccountRepository.findById(1L);
        var bankAccount2 = bankAccountRepository.findById(2L);
        var transactionCreateDto = getTransactionCreateDto(bankAccount1, bankAccount2);
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
