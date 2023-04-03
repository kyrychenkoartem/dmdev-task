package com.artem.dao;

import com.artem.model.entity.UtilityAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilityAccountRepository extends JpaRepository<UtilityAccount, Long> {
}
