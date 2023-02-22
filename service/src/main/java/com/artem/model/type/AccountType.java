package com.artem.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountType {

    SAVINGS_ACCOUNT("Saving account"),
    CHECKING_ACCOUNT("Checking account"),
    FIXED_DEPOSIT("Fixed account"),
    LOAN_ACCOUNT("Loan account");

    private final String name;
}
