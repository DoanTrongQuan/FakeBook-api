package com.example.profileservice.service.iservice;

import com.example.profileservice.dto.request.CreateUserProfileRequest;
import com.example.profileservice.dto.response.CreateUserProfileResponse;

public interface IUserProfileService {
    CreateUserProfileResponse createUserProfile(CreateUserProfileRequest createUserProfileRequest) throws Exception;

}
