package com.example.apigateway.repository;

import com.example.apigateway.dto.ApiResponse;
import com.example.apigateway.dto.response.FilterTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IdentityClient {
    @PostExchange (url = "/auth/filter-token", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<FilterTokenResponse> filterToken(@RequestBody String token);
}
