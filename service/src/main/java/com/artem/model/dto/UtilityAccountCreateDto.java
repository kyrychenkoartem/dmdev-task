package com.artem.model.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UtilityAccountCreateDto(Long accountId,
                                      @NotBlank(message = "Number shouldn't be empty")
                                      String number,
                                      @NotBlank(message = "Provider name shouldn't be empty")
                                      String providerName) {
}
