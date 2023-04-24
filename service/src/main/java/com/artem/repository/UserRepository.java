package com.artem.repository;

import com.artem.model.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,
        CustomUserRepository,
        QuerydslPredicateExecutor<User> {

    Optional<User> findByEmail(String email);
}
