package com.artem.dao;

import com.artem.model.entity.FundTransfer;
import javax.persistence.EntityManager;

public class FundTransferRepository extends RepositoryBase<Long, FundTransfer> {

    public FundTransferRepository(EntityManager entityManager) {
        super(FundTransfer.class, entityManager);
    }
}
