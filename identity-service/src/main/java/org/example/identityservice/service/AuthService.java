package org.example.identityservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.identityservice.dto.request.CreateUserProfileRequest;

import org.example.identityservice.dto.request.CreateUserRequest;
import org.example.identityservice.mapper.ProfileMapper;
import org.example.identityservice.repository.httpclient.ProfileClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthService implements  IAuthService{

    ProfileClient profileClient;
    ProfileMapper profileMapper;


    @Override
    public void createUser(CreateUserRequest createUserRequest) {

        var createProfileUser = profileMapper.toCreateUserProfileRequest(createUserRequest);

        var profile = profileClient.createProfile(createProfileUser);

    }
}
