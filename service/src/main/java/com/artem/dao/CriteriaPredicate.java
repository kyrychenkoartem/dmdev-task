package com.artem.dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.SessionFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriteriaPredicate {

    private List<Predicate> predicates = new ArrayList<>();

    public static CriteriaPredicate builder() {
        return new CriteriaPredicate();
    }

    public static CriteriaBuilder builder(SessionFactory sessionFactory) {
        return sessionFactory.getCriteriaBuilder();
    }

    public <T> CriteriaPredicate add(Predicate predicate, T object) {
        if (ObjectUtils.isNotEmpty(object)) {
            predicates.add(predicate);
        }
        return this;
    }

    public List<Predicate> build() {
        return predicates;
    }
}
