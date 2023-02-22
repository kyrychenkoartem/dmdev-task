package com.artem.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {

    PAYMENT("Payment"),
    REFUND("Refund"),
    TRANSFER("Transfer"),
    DEPOSIT("Deposit");

    private final String name;
}
