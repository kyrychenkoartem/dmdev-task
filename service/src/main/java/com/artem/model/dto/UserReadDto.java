package com.artem.model.dto;

import com.artem.model.type.Role;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UserReadDto(Long id,
                          String firstName,
                          String lastName,
                          String email,
                          String image,
                          LocalDate birthDate,
                          Role role) {
}
