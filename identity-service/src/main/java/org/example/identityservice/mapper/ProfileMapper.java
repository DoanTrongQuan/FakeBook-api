package org.example.identityservice.mapper;

import org.example.identityservice.dto.request.CreateUserProfileRequest;
import org.example.identityservice.dto.request.CreateUserRequest;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    CreateUserProfileRequest toCreateUserProfileRequest(CreateUserRequest createUserRequest);
}
