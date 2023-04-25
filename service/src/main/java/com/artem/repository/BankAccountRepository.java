package com.artem.repository;

import com.artem.model.entity.BankAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Override
    @EntityGraph(attributePaths = {"bankCards", "account", "account.user"})
    Optional<BankAccount> findById(Long id);

    @Query("select ba from BankAccount ba " +
            "left join ba.account ac " +
            "left join ac.user u " +
            "where u.id = :userId")
    List<BankAccount> findAllByUserId(Long userId);
}
