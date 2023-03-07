package com.artem.dao;

import com.artem.model.dto.TransactionFilter;
import com.artem.model.entity.Transaction;
import com.artem.util.EntityGraphUtil;
import com.querydsl.jpa.impl.JPAQuery;
import java.math.BigDecimal;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.jpa.QueryHints;

import static com.artem.model.entity.QAccount.account;
import static com.artem.model.entity.QBankAccount.bankAccount;
import static com.artem.model.entity.QTransaction.transaction;
import static com.artem.model.entity.QUser.user;
import static com.artem.model.entity.QUtilityAccount.utilityAccount;
import static com.artem.model.entity.QUtilityPayment.utilityPayment;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionDaoQuery {

    private static final TransactionDaoQuery INSTANCE = new TransactionDaoQuery();

    /**
     * Return all transaction for each user
     */
    public List<Transaction> getTransactionsByUser(Session session, Long userId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByUser(session);
        return new JPAQuery<Transaction>(session)
                .select(transaction)
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .join(bankAccount.account, account)
                .join(account.user, user)
                .where(user.id.eq(userId))
                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .fetch();
    }

    public List<Transaction> getTransactionsByBankAccount(Session session, Long bankAccountId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByBankAccount(session);
        return new JPAQuery<Transaction>(session)
                .select(transaction)
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .where(bankAccount.id.eq(bankAccountId))
                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .fetch();
    }

    public List<Transaction> getTransactionByUtilityAccountName(Session session, String utilityAccountName) {
        return new JPAQuery<Transaction>(session)
                .select(transaction)
                .from(utilityAccount, utilityAccount)
                .join(utilityAccount.utilityPayments, utilityPayment)
                .join(utilityPayment.transaction, transaction)
                .where(utilityAccount.providerName.eq(utilityAccountName))
                .fetch();
    }

    public BigDecimal getSumTransactionsPaymentByBankAccount(Session session, Long bankAccountId) {
        return new JPAQuery<BigDecimal>(session)
                .select(transaction.amount.sum())
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .where(bankAccount.id.eq(bankAccountId))
                .fetchOne();
    }

    public List<Transaction> getLimitedTransactionsByBankAccountOrderedByTimeAsc(Session session, Long bankAccountId, int limit) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByBankAccount(session);
        return new JPAQuery<Transaction>(session)
                .select(transaction)
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .where(bankAccount.id.eq(bankAccountId))
                .limit(limit)
                .orderBy(transaction.time.asc())
                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .fetch();
    }

    public List<Transaction> getTransactionsByUserOrderedByTimeDesc(Session session, Long userId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByUser(session);
        return new JPAQuery<Transaction>(session)
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

    public List<Transaction> getTransactionsByUserByLastDate(
            Session session, Long userId, TransactionFilter filter) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByUser(session);
        var predicate = QPredicate.builder()
                .add(filter.getReferenceNumber(), transaction.referenceNumber::eq)
                .add(filter.getTime(), transaction.time::after)
                .buildAnd();
        return new JPAQuery<Transaction>(session)
                .select(transaction)
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .join(bankAccount.account, account)
                .join(account.user, user)
                .where(user.id.eq(userId).and(predicate))
                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .fetch();
    }


    public static TransactionDaoQuery getInstance() {
        return INSTANCE;
    }
}
