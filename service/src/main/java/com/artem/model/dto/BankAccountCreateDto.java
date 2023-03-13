package com.artem.model.dto;

import com.artem.model.entity.Account;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record BankAccountCreateDto(Account account,
                                   String number,
                                   AccountType accountType,
                                   AccountStatus accountStatus,
                                   BigDecimal availableBalance,
                                   BigDecimal actualBalance) {
}
