package com.artem.dao;

import com.artem.model.entity.User;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final EntityManager entityManager;

    @Override
    public void deleteUser(User entity) {
        entityManager.merge(entity);
        entityManager.flush();
    }
}
