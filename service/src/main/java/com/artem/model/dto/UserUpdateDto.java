package com.artem.model.dto;

import com.artem.model.type.Role;
import lombok.Builder;

@Builder
public record UserUpdateDto(String firstname,
                            String lastname,
                            String password,
                            Role role) {
}
