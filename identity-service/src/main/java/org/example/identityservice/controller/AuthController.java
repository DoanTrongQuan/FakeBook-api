package org.example.identityservice.controller;


import lombok.RequiredArgsConstructor;
import org.example.identityservice.dto.ApiResponse;
import org.example.identityservice.dto.request.CreateUserRequest;
import org.example.identityservice.exceptions.DataNotFoundException;
import org.example.identityservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
