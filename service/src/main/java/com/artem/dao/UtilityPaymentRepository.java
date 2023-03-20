package com.artem.dao;

import com.artem.model.entity.UtilityPayment;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class UtilityPaymentRepository extends RepositoryBase<Long, UtilityPayment> {

    public UtilityPaymentRepository(EntityManager entityManager) {
        super(UtilityPayment.class, entityManager);
    }
}
