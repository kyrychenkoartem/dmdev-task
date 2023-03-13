package com.artem.model.dto;

import com.artem.model.entity.BankAccount;
import com.artem.model.type.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TransactionCreateDto(BigDecimal amount,
                                   TransactionType type,
                                   String referenceNumber,
                                   String transactionId,
                                   LocalDateTime time,
                                   BankAccount bankAccount) {
}
