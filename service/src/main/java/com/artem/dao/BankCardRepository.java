package com.artem.dao;

import com.artem.model.entity.BankCard;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class BankCardRepository extends RepositoryBase<Long, BankCard> {

    public BankCardRepository(EntityManager entityManager) {
        super(BankCard.class, entityManager);
    }

}
