package com.artem.model.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record BankCardUpdateDto(Long bankAccountId,

                                @NotBlank(message = "Expiry date shouldn't be empty")
                                String expiryDate) {
}
