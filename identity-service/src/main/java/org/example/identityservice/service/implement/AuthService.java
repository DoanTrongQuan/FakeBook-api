package org.example.identityservice.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.example.identityservice.dto.request.CreateUserRequest;
import org.example.identityservice.dto.request.LoginRequest;
import org.example.identityservice.dto.response.FilterTokenResponse;
import org.example.identityservice.dto.response.LoginResponse;
import org.example.identityservice.dto.response.TokenResponse;
import org.example.identityservice.entity.User;
import org.example.identityservice.entity.UserRole;
import org.example.identityservice.exceptions.DataNotFoundException;
import org.example.identityservice.mapper.ProfileMapper;
import org.example.identityservice.mapper.UserMapper;
import org.example.identityservice.repository.RoleRepo;
import org.example.identityservice.repository.UserRepo;
import org.example.identityservice.repository.UserRoleRepo;
import org.example.identityservice.repository.httpclient.ProfileClient;
import org.example.identityservice.service.iservice.IAuthService;
import org.example.identityservice.utils.JwtTokenUtils;
import org.example.identityservice.utils.MessageKeys;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Filter;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public  class AuthService implements IAuthService {

    ProfileClient profileClient;
    ProfileMapper profileMapper;
    UserRepo userRepo;
    UserRoleRepo userRoleRepo;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    AuthenticationManager authenticationManager;
    JwtTokenUtils jwtTokenUtils;

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


    private TokenResponse createTokenResponse(User user) throws Exception {

        String accessToken = jwtTokenUtils.generateToken(user);
        //todo get Expiration of token
        Date access_expired_at = jwtTokenUtils.extractExpiration(accessToken);
        //todo generate refresh token
        String refreshToken = jwtTokenUtils.generateRefreshToken(user);
        //todo get Expiration of refresh token
        Date refresh_expired_at = jwtTokenUtils.extractExpiration(refreshToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .access_expired_at(access_expired_at)
                .refreshToken(refreshToken)
                .refresh_expired_at(refresh_expired_at)
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        User existingUser = userRepo.findByEmail(loginRequest.getEmail()).orElse(null);

        log.info("existingUser :" + existingUser);
        if(existingUser == null) {
            throw new DataNotFoundException(MessageKeys.USER_DOES_NOT_EXITS);
        }

        //Chuyền email,password, role vào authenticationToken để xac thực ngươi dùng
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword(),
                existingUser.getAuthorities()
        );

        //Xác thực người dùng (nếu xác thực không thành công VD: sai pass ) thì sẽ ném ra ngoại lệ
        authenticationManager.authenticate(authenticationToken);

        //todo generate token and refresh token
        TokenResponse tokenResponse = createTokenResponse(existingUser);

        //todo get roles
        Set<UserRole> userRoles = userRoleRepo.findAllByUser(existingUser);

        return LoginResponse.builder()
                .email(existingUser.getEmail())
                .roles(userRoles.stream().map(role ->
                     role.getRole().getRoleName()).collect(Collectors.toList()))
                .dataToken(tokenResponse)
                .build();
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) throws Exception {

        return null;
    }

    @Override
    public FilterTokenResponse filterToken(String token)  {
        String email = jwtTokenUtils.extractEmail(token);
        log.info("Email: {}", email);
        User user = userRepo.findByEmail(email).orElse(null);
        if (user != null) {
            if(jwtTokenUtils.validateToken(token, user)) {


                Set<UserRole>  userRoles = userRoleRepo.findAllByUser(user);
                user.setUserRoles( userRoles.isEmpty() ? null : userRoles );

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            return FilterTokenResponse.builder()
                    .valid(true)
                    .build();
        }
        return FilterTokenResponse.builder()
                .valid(false)
                .build();
    }
}
