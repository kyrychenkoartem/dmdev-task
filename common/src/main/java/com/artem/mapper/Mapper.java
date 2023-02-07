package com.artem.mapper;

import java.util.Optional;

public interface Mapper<E, D> {

    E toEntity(D dto);

    D toDto(E entity);
}
