package com.artem.repository;

import com.artem.model.entity.UtilityPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilityPaymentRepository extends JpaRepository<UtilityPayment, Long> {
}
