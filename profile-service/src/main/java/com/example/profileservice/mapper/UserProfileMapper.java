package com.example.profileservice.mapper;

import com.example.profileservice.dto.request.CreateUserProfileRequest;
import com.example.profileservice.dto.response.CreateUserProfileResponse;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(CreateUserProfileRequest createUserProfileRequest);

    CreateUserProfileResponse toCreateUserProfileResponse(UserProfile userProfile);

    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
}
