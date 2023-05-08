package com.artem.model.dto;

import com.artem.model.type.TransactionStatus;
import com.artem.model.type.TransactionType;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import lombok.Builder;

@Builder
public record TransactionUpdateDto(@DecimalMin(value = "0.00")
                                   BigDecimal amount,
                                   TransactionType type,
                                   TransactionStatus status) {
}
