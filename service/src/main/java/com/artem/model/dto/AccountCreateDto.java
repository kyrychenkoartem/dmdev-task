package com.artem.model.dto;

import com.artem.model.entity.User;
import lombok.Builder;

@Builder
public record AccountCreateDto(User user) {
}
