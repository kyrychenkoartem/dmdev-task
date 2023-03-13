package com.artem.model.dto;


import com.artem.model.entity.UtilityAccount;
import com.artem.model.type.TransactionStatus;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record UtilityPaymentCreateDto(BigDecimal amount,
                                      String account,
                                      TransactionStatus status,
                                      UtilityAccount toAccount,
                                      String transactionId) {
}
