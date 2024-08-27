package org.example.identityservice.service.iservice;

import org.example.identityservice.dto.request.CreateUserRequest;
import org.example.identityservice.dto.request.LoginRequest;
import org.example.identityservice.dto.response.LoginResponse;
import org.example.identityservice.dto.response.TokenResponse;

public interface IAuthService {
    String createUser(CreateUserRequest createUserRequest) throws Exception;
    LoginResponse login(LoginRequest loginRequest) throws Exception;
    TokenResponse refreshToken(String refreshToken) throws Exception;
}
