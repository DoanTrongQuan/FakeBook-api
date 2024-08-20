package org.example.identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserProfileResponse {
    String userId;
    String username;
    String email;
    String firstName;
    String lastName;
    LocalDate dob;
    String city;
}
