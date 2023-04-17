package com.artem.model.dto;

import lombok.Builder;

@Builder
public record UtilityAccountReadDto(Long id,
                                    Long accountId,
                                    String number,
                                    String providerName) {
}
