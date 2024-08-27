package com.example.profileservice.controller;
import com.example.profileservice.dto.ApiResponse;
import com.example.profileservice.dto.request.CreateUserProfileRequest;
import com.example.profileservice.dto.request.UpdateUserProfileRequest;
import com.example.profileservice.dto.response.CreateUserProfileResponse;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.entity.UserProfile;
import com.example.profileservice.exceptions.DataNotFoundException;
import com.example.profileservice.service.implement.UserProfileService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateUserProfileRequest updateUserProfileRequest) throws Exception {
        try {
            UserProfile response = userProfileService.updateUserProfile(updateUserProfileRequest);
            return ResponseEntity.ok(ApiResponse.<UserProfile>builder()
                    .result(response)
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase()).build());
        }catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
