package org.example.identityservice.service;

import org.example.identityservice.dto.request.CreateUserProfileRequest;

public interface IAuthService {
    String createUser(CreateUserProfileRequest createUserProfileRequest);
}
