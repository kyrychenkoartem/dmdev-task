package com.artem.dao;

import com.artem.model.entity.User;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends RepositoryBase<Long, User> {
    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Override
    public void delete(User entity) {
        getEntityManager().merge(entity);
        getEntityManager().flush();
    }
}
