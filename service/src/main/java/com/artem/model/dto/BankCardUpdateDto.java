package com.artem.model.dto;

import com.artem.model.entity.BankAccount;
import lombok.Builder;

@Builder
public record BankCardUpdateDto(BankAccount bankAccount,
                                String expiryDate) {
}
