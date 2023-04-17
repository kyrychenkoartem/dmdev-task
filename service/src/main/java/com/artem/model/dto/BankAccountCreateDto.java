package com.artem.model.dto;


import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record BankAccountCreateDto(Long accountId,
                                   @NotBlank(message = "Number shouldn't be empty")
                                   String number,
                                   AccountType type,
                                   AccountStatus status,
                                   @DecimalMin(value = "0.00")
                                   BigDecimal availableBalance,
                                   @DecimalMin(value = "0.00")
                                   BigDecimal actualBalance) {
}
