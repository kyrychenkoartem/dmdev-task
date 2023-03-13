package com.artem.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class RepositoryBase<K extends Serializable, E> implements Repository<K, E> {

    private final Class<E> clazz;
    @Getter
    private final EntityManager entityManager;

    public E save(E entity) {
        entityManager.persist(entity);
        return entity;
    }

    public void delete(E entity) {
        entityManager.remove(entity);
        entityManager.flush();
    }


    public void update(E entity) {
        entityManager.merge(entity);
        entityManager.flush();
    }


    public Optional<E> findById(K id, Map<String, Object> properties) {
        return Optional.ofNullable(entityManager.find(clazz, id, properties));
    }


    public List<E> findAll() {
        var criteria = entityManager.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);
        return entityManager.createQuery(criteria)
                .getResultList();
    }

}
