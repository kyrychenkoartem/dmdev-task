package com.artem.mapper;

public interface Mapper<F, T> {

    T mapFrom(F object);

}