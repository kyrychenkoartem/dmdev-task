package com.artem.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardType {

    DEBIT("Debit"),
    CREDIT("Credit");

    private final String name;
}