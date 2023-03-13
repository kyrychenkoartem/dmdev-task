package com.artem.model.dto;

import com.artem.model.type.TransactionStatus;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record FundTransferCreateDto(String fromAccount,
                                    String toAccount,
                                    BigDecimal amount,
                                    TransactionStatus status,
                                    String transactionId) {
}
