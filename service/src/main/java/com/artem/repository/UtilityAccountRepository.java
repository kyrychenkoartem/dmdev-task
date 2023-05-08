package com.artem.repository;

import com.artem.model.entity.UtilityAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilityAccountRepository extends JpaRepository<UtilityAccount, Long> {

    @Override
    @EntityGraph(attributePaths = {"account", "utilityPayments"})
    Optional<UtilityAccount> findById(Long id);

    @Query("select ua from UtilityAccount ua " +
            "left join ua.account ac " +
            "left join ac.user u " +
            "where u.id = :userId ")
    List<UtilityAccount> findAllByUserId(Long userId);
}
