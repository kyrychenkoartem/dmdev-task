package com.artem.util;

import com.artem.model.entity.Account;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.Transaction;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.graph.RootGraph;

@UtilityClass
public class EntityGraphUtil {

    public static RootGraph<Transaction> getTransactionGraphByUser(Session session) {
        var entityGraph = session.createEntityGraph(Transaction.class);
        entityGraph.addAttributeNodes("bankAccount");
        var bankAccountSubGraph = entityGraph.addSubgraph("bankAccount", BankAccount.class);
        bankAccountSubGraph.addAttributeNodes("account");
        var accountSubGraph = bankAccountSubGraph.addSubgraph("account", Account.class);
        accountSubGraph.addAttributeNodes("user");
        return entityGraph;
    }

    public static RootGraph<Transaction> getTransactionGraphByBankAccount(Session session) {
        var entityGraph = session.createEntityGraph(Transaction.class);
        entityGraph.addAttributeNodes("bankAccount");
        return entityGraph;
    }
}
