package com.github.prajjwal.hospitalmanagement.controller;

import com.github.prajjwal.hospitalmanagement.dto.LoginRequestDto;
import com.github.prajjwal.hospitalmanagement.dto.LoginResponseDto;
import com.github.prajjwal.hospitalmanagement.dto.SignupResponseDto;
import com.github.prajjwal.hospitalmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody LoginRequestDto signupRequest) {
        return ResponseEntity.ok(authService.signup(signupRequest));
    }
}