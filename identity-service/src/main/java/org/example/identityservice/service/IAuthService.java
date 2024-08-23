package org.example.identityservice.service;

import org.example.identityservice.dto.request.CreateUserRequest;

public interface IAuthService {
    String createUser(CreateUserRequest createUserRequest) throws Exception;
}
