package com.artem.dao;

import com.artem.model.dto.TransactionFilter;
import com.artem.model.entity.BankAccount_;
import com.artem.model.entity.Transaction;
import com.artem.model.entity.Transaction_;
import com.artem.model.entity.User_;
import com.artem.util.EntityGraphUtil;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.jpa.QueryHints;

import static com.artem.model.entity.Account_.user;
import static com.artem.model.entity.BankAccount_.account;
import static com.artem.model.entity.Transaction_.bankAccount;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionDaoCriteria {

    private static final TransactionDaoCriteria INSTANCE = new TransactionDaoCriteria();

    public List<Transaction> getTransactionsByUser(Session session, Long userId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByUser(session);
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Transaction.class);
        var transaction = criteria.from(Transaction.class);
        var bankAccountJoin = transaction.join(bankAccount);
        var accountJoin = bankAccountJoin.join(account);
        var userJoin = accountJoin.join(user);

        criteria.select(transaction)
                .where(cb.equal(userJoin.get(User_.ID), userId));

        return session.createQuery(criteria).setHint(QueryHints.HINT_FETCHGRAPH, entityGraph).list();
    }

    public List<Transaction> getTransactionsByBankAccount(Session session, Long bankAccountId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByBankAccount(session);
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Transaction.class);
        var transaction = criteria.from(Transaction.class);
        var bankAccountJoin = transaction.join(bankAccount);

        criteria.select(transaction)
                .where(cb.equal(bankAccountJoin.get(BankAccount_.ID), bankAccountId));

        return session.createQuery(criteria).setHint(QueryHints.HINT_FETCHGRAPH, entityGraph).list();
    }

    public BigDecimal getSumTransactionsPaymentByBankAccount(Session session, Long bankAccountId) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(BigDecimal.class);
        var transaction = criteria.from(Transaction.class);
        var bankAccountJoin = transaction.join(bankAccount);

        criteria.select(cb.sum(transaction.get(Transaction_.amount)))
                .where(cb.equal(bankAccountJoin.get(BankAccount_.ID), bankAccountId));
        return session.createQuery(criteria).uniqueResult();
    }

    public List<Transaction> getLimitedTransactionsByBankAccountOrderedByTimeAsc(Session session, Long bankAccountId, int limit) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByBankAccount(session);
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Transaction.class);
        var transaction = criteria.from(Transaction.class);
        var bankAccountJoin = transaction.join(bankAccount);

        criteria.select(transaction)
                .where(cb.equal(bankAccountJoin.get(BankAccount_.ID), bankAccountId))
                .orderBy(cb.asc(transaction.get(Transaction_.time)));
        return session.createQuery(criteria)
                .setHint(QueryHints.HINT_FETCHGRAPH, entityGraph)
                .setMaxResults(limit)
                .list();
    }

    public List<Transaction> getTransactionsByUserOrderedByTimeDesc(Session session, Long userId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByUser(session);
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Transaction.class);
        var transaction = criteria.from(Transaction.class);
        var bankAccountJoin = transaction.join(bankAccount);
        var accountJoin = bankAccountJoin.join(account);
        var userJoin = accountJoin.join(user);

        criteria.select(transaction)
                .where(cb.equal(userJoin.get(User_.ID), userId))
                .orderBy(cb.desc(transaction.get(Transaction_.time)));

        return session.createQuery(criteria).setHint(QueryHints.HINT_FETCHGRAPH, entityGraph).list();
    }

    public List<Transaction> getTransactionsByUserByLastDate(
            Session session, Long userId, TransactionFilter filter) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByUser(session);
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Transaction.class);
        var transaction = criteria.from(Transaction.class);
        var bankAccountJoin = transaction.join(bankAccount);
        var accountJoin = bankAccountJoin.join(account);
        var userJoin = accountJoin.join(user);

        var predicates = CriteriaPredicate.builder()
                .add(filter.getReferenceNumber(), it -> cb.equal(transaction.get(Transaction_.referenceNumber), it))
                .add(filter.getTime(), it -> cb.greaterThan(transaction.get(Transaction_.time), it))
                .add(userId, it -> cb.equal(userJoin.get(User_.ID), it))
                .build();

        criteria.select(transaction)
                .where(predicates.toArray(Predicate[]::new));

        return session.createQuery(criteria).setHint(QueryHints.HINT_FETCHGRAPH, entityGraph).list();
    }


    public static TransactionDaoCriteria getInstance() {
        return INSTANCE;
    }
}
