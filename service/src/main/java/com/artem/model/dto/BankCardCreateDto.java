package com.artem.model.dto;

import com.artem.model.entity.BankAccount;
import com.artem.model.entity.User;
import com.artem.model.type.BankType;
import com.artem.model.type.CardType;
import lombok.Builder;

@Builder
public record BankCardCreateDto(User user,
                                BankAccount bankAccount,
                                String cardNumber,
                                String expiryDate,
                                BankType bank,
                                String cvv,
                                CardType cardType) {
}
