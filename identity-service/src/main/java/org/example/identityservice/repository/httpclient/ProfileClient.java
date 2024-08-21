package org.example.identityservice.repository.httpclient;


import org.example.identityservice.configurations.AuthenticationRequestInterceptor;
import org.example.identityservice.dto.ApiResponse;
import org.example.identityservice.dto.request.CreateUserProfileRequest;
import org.example.identityservice.dto.response.CreateUserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProfileClient {
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<CreateUserProfileResponse> createProfile(@RequestBody CreateUserProfileRequest request);
}
