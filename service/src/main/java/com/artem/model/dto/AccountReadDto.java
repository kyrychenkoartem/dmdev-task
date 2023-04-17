package com.artem.model.dto;

import com.artem.model.type.AccountStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record AccountReadDto(Long id,
                             Long userId,
                             AccountStatus status,
                             LocalDateTime createdAt,
                             String createdBy,
                             LocalDateTime updatedAt,
                             String updatedBy,
                             List<BankAccountReadDto> bankAccounts,
                             List<UtilityAccountReadDto> utilityAccounts) {
}
