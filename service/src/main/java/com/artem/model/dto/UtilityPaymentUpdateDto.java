package com.artem.model.dto;

import com.artem.model.type.TransactionStatus;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record UtilityPaymentUpdateDto(BigDecimal amount,
                                      TransactionStatus status) {
}
