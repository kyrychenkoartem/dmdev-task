package com.artem.model.dto;

import lombok.Builder;

@Builder
public record AccountCreateDto(Long userId) {
}
