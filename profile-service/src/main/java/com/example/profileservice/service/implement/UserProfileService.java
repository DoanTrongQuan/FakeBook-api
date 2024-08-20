package com.example.profileservice.service.implement;

import com.example.profileservice.dto.request.CreateUserProfileRequest;
import com.example.profileservice.dto.response.CreateUserProfileResponse;
import com.example.profileservice.entity.UserProfile;
import com.example.profileservice.mapper.UserProfileMapper;
import com.example.profileservice.repository.UserProfileRepo;
import com.example.profileservice.service.iservice.IUserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService implements IUserProfileService {

    private final UserProfileMapper userProfileMapper;

    @Autowired
    private UserProfileRepo userProfileRepo;
    @Override
    public CreateUserProfileResponse createUserProfile(CreateUserProfileRequest createUserProfileRequest) throws Exception {
        UserProfile userProfile = userProfileMapper.toUserProfile(createUserProfileRequest);
        userProfile = userProfileRepo.save(userProfile);

        return userProfileMapper.toCreateUserProfileResponse(userProfile);
    }
}
