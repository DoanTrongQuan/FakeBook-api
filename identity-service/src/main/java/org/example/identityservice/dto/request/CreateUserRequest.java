package org.example.identityservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;
    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_IS_REQUIRED")
    String email;
    String firstName;
    String lastName;

    LocalDate dob;

    String city;
}
