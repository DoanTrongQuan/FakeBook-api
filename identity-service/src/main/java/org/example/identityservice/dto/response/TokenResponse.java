package org.example.identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenResponse {
    String accessToken;
    Date access_expired_at;
    String refreshToken;
    Date refresh_expired_at;
}
