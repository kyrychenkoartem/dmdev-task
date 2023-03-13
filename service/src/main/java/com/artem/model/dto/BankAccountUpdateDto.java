package com.artem.model.dto;

import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record BankAccountUpdateDto(AccountType accountType,
                                   AccountStatus accountStatus,
                                   BigDecimal availableBalance,
                                   BigDecimal actualBalance) {
}
