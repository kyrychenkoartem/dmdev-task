package com.artem.model.dto;

import com.artem.model.type.BankType;
import com.artem.model.type.CardType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;

@Builder
public record BankCardCreateDto(Long userId,

                                Long bankAccountId,

                                @NotBlank(message = "Card number shouldn't be empty")
                                @Size(min = 16, max = 16)
                                String cardNumber,

                                @NotBlank(message = "Expiry date shouldn't be empty")
                                String expiryDate,

                                @NotBlank(message = "Cvv shouldn't be empty")
                                @Size(min = 3, max = 4)
                                String cvv,

                                BankType bank,

                                CardType type) {
}
