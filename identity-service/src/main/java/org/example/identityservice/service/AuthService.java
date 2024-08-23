package org.example.identityservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.example.identityservice.dto.request.CreateUserRequest;
import org.example.identityservice.entity.User;
import org.example.identityservice.entity.UserRole;
import org.example.identityservice.exceptions.DataNotFoundException;
import org.example.identityservice.mapper.ProfileMapper;
import org.example.identityservice.mapper.UserMapper;
import org.example.identityservice.repository.RoleRepo;
import org.example.identityservice.repository.UserRepo;
import org.example.identityservice.repository.UserRoleRepo;
import org.example.identityservice.repository.httpclient.ProfileClient;
import org.example.identityservice.utils.MessageKeys;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthService implements  IAuthService{

    ProfileClient profileClient;
    ProfileMapper profileMapper;
    UserRepo userRepo;
    UserRoleRepo userRoleRepo;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;


    @Override
    @Transactional
    public String createUser(CreateUserRequest createUserRequest) throws Exception {

        User user = userRepo.findByEmail(createUserRequest.getEmail()).orElse(null);
        if(user != null) {
            throw new DataIntegrityViolationException(MessageKeys.USER_EARLY_EXITS);
        }
        //todo build user entity to save db
        user = User.builder()
                .uuid(String.valueOf(UUID.randomUUID()))
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .email(createUserRequest.getEmail())
                .emailVerified(true)
                .username(createUserRequest.getUsername())
                .build();

        UserRole userRole = UserRole.builder()
                //todo save user default with role USER
                .role(roleRepo.findById(1).orElseThrow(() -> new DataNotFoundException(MessageKeys.ROLE_DOES_NOT_EXITS)))
                .user(user)
                .build();

        var createProfileUser = profileMapper.toCreateUserProfileRequest(createUserRequest);
        createProfileUser.setUserId(user.getUuid());

        //toto call api to profile-service
        profileClient.createProfile(createProfileUser);

        //todo save user to db
        userRepo.save(user);

        userRoleRepo.save(userRole);
        return "Create successful";
    }
}
