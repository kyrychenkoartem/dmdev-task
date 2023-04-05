package com.artem.repository;

import com.artem.model.dto.TransactionFilter;
import com.artem.model.entity.BankAccount_;
import com.artem.model.entity.Transaction;
import com.artem.model.entity.Transaction_;
import com.artem.model.entity.User_;
import com.artem.util.EntityGraphUtil;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import static com.artem.model.entity.Account_.user;
import static com.artem.model.entity.BankAccount_.account;
import static com.artem.model.entity.Transaction_.bankAccount;

@Repository
public class TransactionDaoCriteria {


    public List<Transaction> getTransactionsByUser(EntityManager session, Long userId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByUser(session);
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Transaction.class);
        var transaction = criteria.from(Transaction.class);
        var bankAccountJoin = transaction.join(bankAccount);
        var accountJoin = bankAccountJoin.join(account);
        var userJoin = accountJoin.join(user);

        criteria.select(transaction)
                .where(cb.equal(userJoin.get(User_.ID), userId));

        return session.createQuery(criteria).setHint(QueryHints.HINT_FETCHGRAPH, entityGraph).getResultList();
    }

    public List<Transaction> getTransactionsByBankAccount(EntityManager session, Long bankAccountId) {
        var entityGraph = EntityGraphUtil.getTransactionGraphByBankAccount(session);
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Transaction.class);
        var transaction = criteria.from(Transaction.class);
        var bankAccountJoin = transaction.join(bankAccount);

        criteria.select(transaction)
                .where(cb.equal(bankAccountJoin.get(BankAccount_.ID), bankAccountId));

        return session.createQuery(criteria).setHint(QueryHints.HINT_FETCHGRAPH, entityGraph).getResultList();
    }

    public BigDecimal getSumTransactionsPaymentByBankAccount(EntityManager session, Long bankAccountId) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(BigDecimal.class);
        var transaction = criteria.from(Transaction.class);
        var bankAccountJoin = transaction.join(bankAccount);

        criteria.select(cb.sum(transaction.get(Transaction_.amount)))
                .where(cb.equal(bankAccountJoin.get(BankAccount_.ID), bankAccountId));
        return session.createQuery(criteria).getSingleResult();
    }

    public List<Transaction> getLimitedTransactionsByBankAccountOrderedByTimeAsc(EntityManager session, Long bankAccountId, int limit) {
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
                .getResultList();
    }

    public List<Transaction> getTransactionsByUserOrderedByTimeDesc(EntityManager session, Long userId) {
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

        return session.createQuery(criteria).setHint(QueryHints.HINT_FETCHGRAPH, entityGraph).getResultList();
    }

    public List<Transaction> getTransactionsByUserByLastDate(
            EntityManager session, Long userId, TransactionFilter filter) {
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

        return session.createQuery(criteria).setHint(QueryHints.HINT_FETCHGRAPH, entityGraph).getResultList();
    }


//    public static TransactionDaoCriteria getInstance() {
//        return INSTANCE;
//    }
}
