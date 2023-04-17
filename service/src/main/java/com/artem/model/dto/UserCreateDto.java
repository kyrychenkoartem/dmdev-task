package com.artem.model.dto;

import com.artem.model.type.Role;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record UserCreateDto(@NotBlank(message = "First name shouldn't be empty")
                            @Size(min = 3, max = 64)
                            String firstname,
                            @NotBlank(message = "Last name shouldn't be empty")
                            @Size(min = 3, max = 64)
                            String lastname,
                            @Email
                            String email,
                            @Size(min = 6)
                            String password,
                            LocalDate birthDate,

                            Role role,

                            MultipartFile image) {
}
