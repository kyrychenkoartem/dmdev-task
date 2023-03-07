package com.artem.dao;


import com.artem.model.dto.TransactionFilter;
import com.artem.model.entity.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class TransactionDaoQueryTest extends DaoTestBase {

    private final TransactionDaoQuery transactionDaoQuery = TransactionDaoQuery.getInstance();

    @Test
    void checkGetTransactionByUser() {
        var transactions = transactionDaoQuery.getTransactionsByUser(session, 1L);

        var userEmail = transactions.stream()
                .map(it -> it.getBankAccount().getAccount().getUser().getEmail())
                .findFirst();

        assertThat(transactions).hasSize(14);
        assertThat(userEmail.get()).isEqualTo("ivan@gmail.com");
    }

    @Test
    void checkGetTransactionsByBankAccount() {
        var transactions = transactionDaoQuery.getTransactionsByBankAccount(session, 10L);

        var bankAccountNumber = transactions.stream()
                .map(it -> it.getBankAccount().getNumber())
                .findFirst();

        assertThat(transactions).hasSize(4);
        assertThat(bankAccountNumber.get()).isEqualTo("0123456789");
    }

    @Test
    void checkGetTransactionByUtilityAccountName() {
        var transactions = transactionDaoQuery.getTransactionByUtilityAccountName(session, "Koodo");

        assertThat(transactions).hasSize(4);
    }

    @Test
    void checkGetSumTransactionsPaymentByBankAccount() {
        var sumTransactionsPayment = transactionDaoQuery.getSumTransactionsPaymentByBankAccount(session, 15L);

        assertThat(sumTransactionsPayment).isEqualTo(BigDecimal.valueOf(150).setScale(2, RoundingMode.CEILING));
    }

    @Test
    void checkGetLimitedTransactionsByBankAccountOrderedByTime() {
        var transactions = transactionDaoQuery.getLimitedTransactionsByBankAccountOrderedByTimeAsc(session, 3L, 10);

        var times = transactions.stream()
                .map(Transaction::getTime)
                .toList();

        assertThat(transactions).hasSize(3);
        assertThat(times.get(0)).isBefore(times.get(2));
    }

    @Test
    void checkGetTransactionsByUserOrderedByTimeDesc() {
        var transactions = transactionDaoQuery.getTransactionsByUserOrderedByTimeDesc(session, 2L);

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
        var transactions = transactionDaoQuery.getTransactionsByUserByLastDate(session, 4L, filter);

        var referenceNumbers = transactions.stream()
                .map(Transaction::getReferenceNumber)
                .toList();
        var times = transactions.stream()
                .map(Transaction::getTime)
                .toList();

        assertTrue(referenceNumbers.stream().anyMatch(it -> it.equals("1134567890")));
        assertTrue(times.stream().allMatch(it -> LocalDateTime.of(1980, 1, 1, 0, 0).isBefore(it)));
    }
}
