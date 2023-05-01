package com.artem.repository;

import com.artem.model.entity.BankCard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankCardRepository extends JpaRepository<BankCard, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "bankAccount"})
    Optional<BankCard> findById(Long id);

    List<BankCard> findAllByUserId(Long userId);
}
