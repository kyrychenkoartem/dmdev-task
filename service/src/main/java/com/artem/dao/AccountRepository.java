package com.artem.dao;

import com.artem.model.entity.Account;
import javax.persistence.EntityManager;

public class AccountRepository extends RepositoryBase<Long, Account> {

    public AccountRepository(EntityManager entityManager) {
        super(Account.class, entityManager);
    }
}
