package com.artem.util;

import com.artem.model.entity.Account;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.Transaction;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EntityGraphUtil {

    public static EntityGraph<Transaction> getTransactionGraphByUser(EntityManager session) {
        var entityGraph = session.createEntityGraph(Transaction.class);
        entityGraph.addAttributeNodes("bankAccount");
        var bankAccountSubGraph = entityGraph.addSubgraph("bankAccount", BankAccount.class);
        bankAccountSubGraph.addAttributeNodes("account");
        var accountSubGraph = bankAccountSubGraph.addSubgraph("account", Account.class);
        accountSubGraph.addAttributeNodes("user");
        return entityGraph;
    }

    public static EntityGraph<Transaction> getTransactionGraphByBankAccount(EntityManager session) {
        var entityGraph = session.createEntityGraph(Transaction.class);
        entityGraph.addAttributeNodes("bankAccount");
        return entityGraph;
    }

    public static EntityGraph<BankAccount> getBankAccountGraph(EntityManager session) {
        var entityGraph = session.createEntityGraph(BankAccount.class);
        entityGraph.addAttributeNodes("bankCards");
        entityGraph.addAttributeNodes("transactions");
        return entityGraph;
    }
}
