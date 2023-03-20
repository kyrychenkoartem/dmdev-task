package com.artem.dao;

import com.artem.model.dto.TransactionFilter;
import com.artem.model.entity.Transaction;
import com.artem.util.EntityGraphUtil;
import com.querydsl.jpa.impl.JPAQuery;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import static com.artem.model.entity.QAccount.account;
import static com.artem.model.entity.QBankAccount.bankAccount;
import static com.artem.model.entity.QTransaction.transaction;
import static com.artem.model.entity.QUser.user;
import static com.artem.model.entity.QUtilityAccount.utilityAccount;
import static com.artem.model.entity.QUtilityPayment.utilityPayment;

@Repository
public class TransactionRepository extends RepositoryBase<Long, Transaction> {

    public TransactionRepository(EntityManager entityManager) {
        super(Transaction.class, entityManager);
    }

    public List<Transaction> getTransactionsByUser(Long userId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByUser(getEntityManager());
        return new JPAQuery<Transaction>(getEntityManager())
                .select(transaction)
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .join(bankAccount.account, account)
                .join(account.user, user)
                .where(user.id.eq(userId))
                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .fetch();
    }

    public List<Transaction> getTransactionsByBankAccount(Long bankAccountId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByBankAccount(getEntityManager());
        return new JPAQuery<Transaction>(getEntityManager())
                .select(transaction)
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .where(bankAccount.id.eq(bankAccountId))
                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .fetch();
    }

    public List<Transaction> getTransactionByUtilityAccountName(String utilityAccountName) {
        return new JPAQuery<Transaction>(getEntityManager())
                .select(transaction)
                .from(transaction)
                .where(transaction.transactionId.in(
                        new JPAQuery<String>(getEntityManager())
                                .select(utilityPayment.transaction)
                                .from(utilityAccount)
                                .where(utilityAccount.providerName.eq(utilityAccountName))
                                .join(utilityAccount.utilityPayments, utilityPayment)
                                .fetch()
                ))
                .groupBy()
                .fetch();
    }

    public BigDecimal getSumTransactionsPaymentByBankAccount(Long bankAccountId) {
        return new JPAQuery<BigDecimal>(getEntityManager())
                .select(transaction.amount.sum())
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .where(bankAccount.id.eq(bankAccountId))
                .fetchOne();
    }

    public List<Transaction> getLimitedTransactionsByBankAccountOrderedByTimeAsc(Long bankAccountId, int limit) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByBankAccount(getEntityManager());
        return new JPAQuery<Transaction>(getEntityManager())
                .select(transaction)
                .from(transaction)
                .join(transaction.bankAccount, bankAccount)
                .where(bankAccount.id.eq(bankAccountId))
                .limit(limit)
                .orderBy(transaction.time.asc())
                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .fetch();
    }

    public List<Transaction> getTransactionsByUserOrderedByTimeDesc(Long userId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByUser(getEntityManager());
        return new JPAQuery<Transaction>(getEntityManager())
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

    public List<Transaction> getTransactionsByUserByLastDate(Long userId, TransactionFilter filter) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByUser(getEntityManager());
        var predicate = QPredicate.builder()
                .add(filter.getReferenceNumber(), transaction.referenceNumber::eq)
                .add(filter.getTime(), transaction.time::after)
                .buildAnd();
        return new JPAQuery<Transaction>(getEntityManager())
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
