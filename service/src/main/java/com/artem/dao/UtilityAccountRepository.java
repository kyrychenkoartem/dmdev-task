package com.artem.dao;

import com.artem.model.entity.UtilityAccount;
import javax.persistence.EntityManager;

public class UtilityAccountRepository extends RepositoryBase<Long, UtilityAccount> {

    public UtilityAccountRepository(EntityManager entityManager) {
        super(UtilityAccount.class, entityManager);
    }
}
