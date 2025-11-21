package com.github.prajjwal.hospitalmanagement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prajjwal.hospitalmanagement.dto.LoginResponseDto;
import com.github.prajjwal.hospitalmanagement.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String regisrationId = token.getAuthorizedClientRegistrationId();

        ResponseEntity<LoginResponseDto> loginResponse =  authService
                .handleOAuth2LoginRequest(oAuth2User, regisrationId);

        response.setStatus(loginResponse.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(loginResponse.getBody()));

        // code bb433b994dc33887847e&state=oE3lhbcZ-3xslw0De0HV1w-5mbivddUvMvk6HlkcWrI%3D
    }
}