package com.artem.model.dto;

import com.artem.model.type.BankType;
import com.artem.model.type.CardType;
import lombok.Builder;

@Builder
public record BankCardCreateDto(Long userId,
                                Long bankAccountId,
                                String cardNumber,
                                String expiryDate,
                                BankType bank,
                                String cvv,
                                CardType cardType) {
}
