package com.artem.model.dto;

import com.artem.model.type.AccountStatus;
import com.artem.model.type.AccountType;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder
public record BankAccountReadDto(Long id,
                                 Long accountId,
                                 String number,
                                 AccountType type,
                                 AccountStatus status,
                                 BigDecimal availableBalance,
                                 BigDecimal actualBalance,
                                 List<BankCardReadDto> bankCards,
                                 List<TransactionReadDto> transactions) {
}
