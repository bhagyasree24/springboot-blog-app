package com.projects.blog.controllers;

import com.projects.blog.domain.dtos.AuthResponse;
import com.projects.blog.domain.dtos.LoginRequest;
import com.projects.blog.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/login")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
            UserDetails userDetails = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            String tokenValue = authService.generateToken(userDetails);
            AuthResponse authResponse = AuthResponse.builder()
                    .token(tokenValue)
                    .expiresIn(86400)
                    .build();
            return ResponseEntity.ok(authResponse);

    }
}
