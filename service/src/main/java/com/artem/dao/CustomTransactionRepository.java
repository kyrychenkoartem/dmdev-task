package com.artem.dao;

import com.artem.model.dto.TransactionFilter;
import com.artem.model.entity.Transaction;
import java.math.BigDecimal;
import java.util.List;

public interface CustomTransactionRepository {

    List<Transaction> getTransactionsByUser(Long userId);

    List<Transaction> getTransactionsByBankAccount(Long bankAccountId);

    List<Transaction> getTransactionByUtilityAccountName(String utilityAccountName);

    BigDecimal getSumTransactionsPaymentByBankAccount(Long bankAccountId);

    List<Transaction> getLimitedTransactionsByBankAccountOrderedByTimeAsc(Long bankAccountId, int limit);

    List<Transaction> getTransactionsByUserOrderedByTimeDesc(Long userId);

    List<Transaction> getTransactionsByUserByLastDate(Long userId, TransactionFilter filter);
}
