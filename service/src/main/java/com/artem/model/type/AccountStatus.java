package com.artem.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountStatus {

    PENDING("Pending"),
    ACTIVE("Active"),
    BLOCKED("Blocked");

    private final String name;
}
