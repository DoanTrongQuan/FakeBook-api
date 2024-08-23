package com.example.profileservice.controller;
import com.example.profileservice.dto.ApiResponse;
import com.example.profileservice.dto.request.CreateUserProfileRequest;
import com.example.profileservice.dto.response.CreateUserProfileResponse;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.service.implement.UserProfileService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Builder
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;


    @PostMapping("/create-profile")
    ApiResponse<CreateUserProfileResponse> createProfile(@RequestBody CreateUserProfileRequest request) throws Exception {
        try {
            CreateUserProfileResponse response = userProfileService.createUserProfile(request);
            return ApiResponse.<CreateUserProfileResponse>builder()
                    .result(response)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
