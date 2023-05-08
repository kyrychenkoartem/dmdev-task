package com.artem.model.dto;

import lombok.Builder;

@Builder
public record BankCardUpdateDto(Long bankAccountId,
                                String expiryDate) {
}
