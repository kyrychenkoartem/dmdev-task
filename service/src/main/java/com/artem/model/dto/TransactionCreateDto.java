package com.artem.model.dto;

import com.artem.model.type.TransactionType;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TransactionCreateDto(@DecimalMin(value = "0.00")
                                   BigDecimal amount,

                                   TransactionType type,

                                   @NotBlank(message = "Transaction id shouldn't be empty")                                   String referenceNumber,
                                   String transactionId,

                                   Long bankAccountId) {
}
