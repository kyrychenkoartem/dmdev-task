package com.artem.model.dto;

import lombok.Builder;

@Builder
public record UtilityAccountCreateDto(String number,
                                      String providerName) {
}
