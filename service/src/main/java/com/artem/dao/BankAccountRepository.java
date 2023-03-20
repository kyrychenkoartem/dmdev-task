package com.artem.dao;

import com.artem.model.entity.BankAccount;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class BankAccountRepository extends RepositoryBase<Long, BankAccount> {

    public BankAccountRepository(EntityManager entityManager) {
        super(BankAccount.class, entityManager);
    }
}
