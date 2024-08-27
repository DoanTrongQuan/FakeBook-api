package org.example.identityservice.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.identityservice.entity.User;
import org.example.identityservice.exceptions.DataNotFoundException;
import org.example.identityservice.repository.UserRepo;
import org.example.identityservice.service.iservice.IUserService;
import org.example.identityservice.utils.MessageKeys;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService implements IUserService {
    UserRepo userRepo;

    @Override
    public String updateUser(int uuidUser) throws Exception {
        User user = userRepo.findById(uuidUser).orElse(null);
        if( user == null ) {
            throw new DataNotFoundException(MessageKeys.USER_DOES_NOT_EXITS);
        }

        return "";
    }

}
