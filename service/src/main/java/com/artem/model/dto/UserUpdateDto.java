package com.artem.model.dto;

import com.artem.model.type.Role;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record UserUpdateDto(@NotBlank(message = "First name shouldn't be empty")
                            @Size(min = 3, max = 64)
                            String firstName,
                            @NotBlank(message = "Last name shouldn't be empty")
                            @Size(min = 3, max = 64)
                            String lastName,
                            @Email
                            String email,
                            LocalDate birthDate,

                            Role role,
                            MultipartFile image) {
}
