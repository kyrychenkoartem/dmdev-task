package com.artem.repository;

import com.artem.model.entity.UtilityAccount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilityAccountRepository extends JpaRepository<UtilityAccount, Long> {

    @Query("select ua from UtilityAccount ua " +
            "left join ua.account ac " +
            "left join ac.user u " +
            "where u.id = :userId ")
    List<UtilityAccount> findAllByUserId(Long userId);
}
