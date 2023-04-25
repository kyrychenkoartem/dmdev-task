package com.artem.model.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UtilityAccountUpdateDto(@NotBlank(message = "Provider name shouldn't be empty")

                                      String providerName) {
}
