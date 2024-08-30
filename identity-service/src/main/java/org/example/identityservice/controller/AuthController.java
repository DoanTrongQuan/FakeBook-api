package org.example.identityservice.controller;


import lombok.RequiredArgsConstructor;
import org.example.identityservice.dto.ApiResponse;
import org.example.identityservice.dto.request.CreateUserRequest;
import org.example.identityservice.dto.request.LoginRequest;
import org.example.identityservice.dto.response.FilterTokenResponse;
import org.example.identityservice.dto.response.LoginResponse;
import org.example.identityservice.exceptions.DataNotFoundException;
import org.example.identityservice.service.implement.AuthService;
import org.example.identityservice.utils.MessageKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/create-user")
    public ResponseEntity<?>  create(@RequestBody CreateUserRequest createUserRequest)  {
        try {
            return ResponseEntity.ok(ApiResponse.<String>builder()
                            .code(HttpStatus.OK.value())
                            .message(authService.createUser(createUserRequest))
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?>  login(@RequestBody LoginRequest loginRequest)  {
        try {
            return ResponseEntity.ok(ApiResponse.<LoginResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(MessageKeys.LOGIN_SUCCESSFULLY)
                    .result(authService.login(loginRequest))
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/filter-token")
    public FilterTokenResponse  filterToken(@RequestBody String token)  {
            return authService.filterToken(token);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-info")
    public String  getInfo()  {
        return "Hello Đoàn Trọng Quân";
    }
}
