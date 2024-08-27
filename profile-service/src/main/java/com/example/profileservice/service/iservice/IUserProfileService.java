package com.example.profileservice.service.iservice;

import com.example.profileservice.dto.ApiResponse;
import com.example.profileservice.dto.request.CreateUserProfileRequest;
import com.example.profileservice.dto.request.UpdateUserProfileRequest;
import com.example.profileservice.dto.response.CreateUserProfileResponse;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.entity.UserProfile;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUserProfileService {
    CreateUserProfileResponse createUserProfile(@RequestBody CreateUserProfileRequest createUserProfileRequest) throws Exception;

    UserProfileResponse getUserInfo(String userId) throws Exception;

    UserProfile updateUserProfile(UpdateUserProfileRequest updateUserProfileRequest) throws Exception;
}
