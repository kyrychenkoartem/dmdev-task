package com.artem.model.dto;

import com.artem.model.type.Role;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UserUpdateDto(String firstName,
                            String lastName,
                            String email,
                            LocalDate birthDate,
                            Role role) {
}
