package com.artem.repository;

import com.artem.model.dto.TransactionFilter;
import com.artem.model.entity.Transaction;
import com.artem.util.EntityGraphUtil;
import com.querydsl.jpa.impl.JPAQuery;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.QueryHints;

import static com.artem.model.entity.QAccount.account;
import static com.artem.model.entity.QBankAccount.bankAccount;
import static com.artem.model.entity.QTransaction.transaction;
import static com.artem.model.entity.QUser.user;
import static com.artem.model.entity.QUtilityAccount.utilityAccount;
import static com.artem.model.entity.QUtilityPayment.utilityPayment;

@RequiredArgsConstructor
public class CustomTransactionRepositoryImpl implements CustomTransactionRepository {

    private final EntityManager entityManager;

    @Override
    public List<Transaction> getTransactionsByUser(Long userId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByUser(entityManager);
        return new JPAQuery<Transaction>(entityManager)
                .select(transaction)
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .join(bankAccount.account, account)
                .join(account.user, user)
                .where(user.id.eq(userId))
                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .fetch();
    }

    @Override
    public List<Transaction> getTransactionsByBankAccount(Long bankAccountId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByBankAccount(entityManager);
        return new JPAQuery<Transaction>(entityManager)
                .select(transaction)
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .where(bankAccount.id.eq(bankAccountId))
                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .fetch();
    }

    @Override
    public List<Transaction> getTransactionByUtilityAccountName(String utilityAccountName) {
        return new JPAQuery<Transaction>(entityManager)
                .select(transaction)
                .from(transaction)
                .where(transaction.transactionId.in(
                        new JPAQuery<String>(entityManager)
                                .select(utilityPayment.transaction)
                                .from(utilityAccount)
                                .where(utilityAccount.providerName.eq(utilityAccountName))
                                .join(utilityAccount.utilityPayments, utilityPayment)
                                .fetch()
                ))
                .groupBy()
                .fetch();
    }

    @Override
    public BigDecimal getSumTransactionsPaymentByBankAccount(Long bankAccountId) {
        return new JPAQuery<BigDecimal>(entityManager)
                .select(transaction.amount.sum())
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .where(bankAccount.id.eq(bankAccountId))
                .fetchOne();
    }

    @Override
    public List<Transaction> getLimitedTransactionsByBankAccountOrderedByTimeAsc(Long bankAccountId, int limit) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByBankAccount(entityManager);
        return new JPAQuery<Transaction>(entityManager)
                .select(transaction)
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .where(bankAccount.id.eq(bankAccountId))
                .limit(limit)
                .orderBy(transaction.time.asc())
                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .fetch();
    }

    @Override
    public List<Transaction> getTransactionsByUserOrderedByTimeDesc(Long userId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByUser(entityManager);
        return new JPAQuery<Transaction>(entityManager)
                .select(transaction)
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .join(bankAccount.account, account)
                .join(account.user, user)
                .where(user.id.eq(userId))
                .orderBy(transaction.time.desc())
                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .fetch();
    }

    @Override
    public List<Transaction> getTransactionsByUserByLastDate(Long userId, TransactionFilter filter) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByUser(entityManager);
        var predicate = QPredicate.builder()
                .add(filter.getReferenceNumber(), transaction.referenceNumber::eq)
                .add(filter.getTime(), transaction.time::after)
                .buildAnd();
        return new JPAQuery<Transaction>(entityManager)
                .select(transaction)
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .join(bankAccount.account, account)
                .join(account.user, user)
                .where(user.id.eq(userId).and(predicate))
                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .fetch();
    }
}
