package com.example.profileservice.service.implement;

import com.example.profileservice.dto.request.CreateUserProfileRequest;
import com.example.profileservice.dto.response.CreateUserProfileResponse;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.entity.UserProfile;
import com.example.profileservice.exceptions.DataNotFoundException;
import com.example.profileservice.mapper.UserProfileMapper;
import com.example.profileservice.repository.UserProfileRepo;
import com.example.profileservice.service.iservice.IUserProfileService;
import com.example.profileservice.utils.MessageKeys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService implements IUserProfileService {

    UserProfileMapper userProfileMapper;

    @Autowired
    private UserProfileRepo userProfileRepo;
    @Override
    public CreateUserProfileResponse createUserProfile(CreateUserProfileRequest createUserProfileRequest) throws Exception {
        UserProfile userProfile = userProfileMapper.toUserProfile(createUserProfileRequest);
        userProfile = userProfileRepo.save(userProfile);

        return userProfileMapper.toCreateUserProfileResponse(userProfile);
    }

    @Override
    public UserProfileResponse getUserInfo(String userId) throws Exception {
        UserProfile userProfile = userProfileRepo.findByUserId(userId);
        if(userProfile == null) {
            throw new DataNotFoundException(MessageKeys.USER_PROFILE_DOES_NOT_EXIST);
        }

        return userProfileMapper.toUserProfileResponse(userProfile);
    }
}
