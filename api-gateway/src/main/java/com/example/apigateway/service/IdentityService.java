package com.example.apigateway.service;

import com.example.apigateway.dto.ApiResponse;
import com.example.apigateway.dto.response.FilterTokenResponse;
import com.example.apigateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<FilterTokenResponse> filterToken(String token) {
        Mono<FilterTokenResponse> filterTokenResponseMono = identityClient.filterToken(token);

        filterTokenResponseMono.flatMap(filterTokenResponse -> {
            if(filterTokenResponse.isValid()) {
                log.info("Test result :  hop le");
                return Mono.just(filterTokenResponse);
            } else {
                log.info("Test result : khong hop le");
                return Mono.just(filterTokenResponse);
            }
        });
        return filterTokenResponseMono;
    }
}
