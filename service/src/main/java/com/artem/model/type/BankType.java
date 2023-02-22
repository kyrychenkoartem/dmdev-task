package com.artem.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BankType {

    CIBC("Canadian Imperial Bank of Commerce"),
    RBC("Royal Bank of Canada"),
    MONTREAL_BANK("The Bank of Montreal"),
    NOVA_SCOTIA_BANK("Scotiabank"),
    TORONTO_DOMINION_BANK("Toronto-Dominion Bank");

    private final String name;
}
