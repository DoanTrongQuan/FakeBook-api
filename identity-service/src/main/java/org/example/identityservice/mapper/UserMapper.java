package org.example.identityservice.mapper;

import org.example.identityservice.dto.request.CreateUserRequest;
import org.example.identityservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(CreateUserRequest createUserRequest);
}
