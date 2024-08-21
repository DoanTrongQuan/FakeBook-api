package org.example.identityservice.service;

import org.example.identityservice.dto.request.CreateUserProfileRequest;
import org.example.identityservice.dto.request.CreateUserRequest;

public interface IAuthService {
    void createUser(CreateUserRequest createUserRequest);
}
