package com.artem.dao;

import com.artem.model.entity.BankCard;
import javax.persistence.EntityManager;

public class BankCardRepository extends RepositoryBase<Long, BankCard> {
    public BankCardRepository(EntityManager entityManager) {
        super(BankCard.class, entityManager);
    }

}
