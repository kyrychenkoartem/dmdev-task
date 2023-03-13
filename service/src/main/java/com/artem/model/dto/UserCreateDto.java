package com.artem.model.dto;

import com.artem.model.type.Role;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UserCreateDto(String firstname,
                            String lastname,
                            String email,
                            String password,
                            LocalDate birthDate,
                            Role role) {
}
