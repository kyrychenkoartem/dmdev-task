package com.artem.repository;

import com.artem.model.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserId(Long userId);

    @Override
    @EntityGraph(attributePaths = {"accounts", "user"})
    Optional<Account> findById(Long id);
}
