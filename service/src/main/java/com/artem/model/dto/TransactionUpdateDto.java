package com.artem.model.dto;

import com.artem.model.type.TransactionType;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record TransactionUpdateDto(BigDecimal amount,
                                   TransactionType type) {
}
