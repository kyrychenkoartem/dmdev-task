package com.artem.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriteriaPredicate {

    private List<Predicate> predicates = new ArrayList<>();

    public static CriteriaPredicate builder() {
        return new CriteriaPredicate();
    }

    public <T> CriteriaPredicate add(T object, Function<T, Predicate>  function) {
        if (ObjectUtils.isNotEmpty(object)) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public List<Predicate> build() {
        return predicates;
    }
}
