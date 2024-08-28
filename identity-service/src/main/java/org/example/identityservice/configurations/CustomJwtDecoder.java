package org.example.identityservice.configurations;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Handle null for issue time
            Instant issueTime = null;
            Date issueDate = signedJWT.getJWTClaimsSet().getIssueTime();
            if (issueDate != null) {
                issueTime = issueDate.toInstant();
            }

            // Handle null for expiration time
            Instant expirationTime = null;
            Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expirationDate != null) {
                expirationTime = expirationDate.toInstant();
            }

            return new Jwt(token,
                    issueTime,
                    expirationTime,
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().getClaims()
            );

        } catch (ParseException e) {
            throw new JwtException("Invalid token");
        }
    }
}
