package com.artem.model.dto;

import com.artem.model.type.AccountStatus;
import lombok.Builder;

@Builder
public record AccountUpdateDto(AccountStatus status) {
}
