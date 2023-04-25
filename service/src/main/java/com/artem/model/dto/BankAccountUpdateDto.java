package com.artem.model.dto;

import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import lombok.Builder;

@Builder
public record BankAccountUpdateDto(AccountType accountType,
                                   AccountStatus accountStatus,
                                   @DecimalMin(value = "0.00")
                                   BigDecimal availableBalance,
                                   @DecimalMin(value = "0.00")
                                   BigDecimal actualBalance) {
}
