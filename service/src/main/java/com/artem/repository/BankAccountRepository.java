package com.artem.repository;

import com.artem.model.entity.BankAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Override
    @EntityGraph(attributePaths = {"bankCards", "account", "account.user"})
    Optional<BankAccount> findById(Long id);
}
