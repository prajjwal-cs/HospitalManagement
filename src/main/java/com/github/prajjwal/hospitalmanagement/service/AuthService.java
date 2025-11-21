package com.github.prajjwal.hospitalmanagement.service;

import com.github.prajjwal.hospitalmanagement.dto.LoginRequestDto;
import com.github.prajjwal.hospitalmanagement.dto.LoginResponseDto;
import com.github.prajjwal.hospitalmanagement.dto.SignupResponseDto;
import com.github.prajjwal.hospitalmanagement.model.User;
import com.github.prajjwal.hospitalmanagement.model.type.AuthProviderType;
import com.github.prajjwal.hospitalmanagement.repository.UserRepository;
import com.github.prajjwal.hospitalmanagement.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);

        return new LoginResponseDto(token, user.getId());
    }

    public User signupInternal(LoginRequestDto signupRequestDto, AuthProviderType authProviderType, String providerId) {
        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);

        if (user != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        user = User.builder()
                .username(signupRequestDto.getUsername())
                .providerId(providerId)
                .providerType(authProviderType)
                .build();

        if (authProviderType == AuthProviderType.EMAIL) {
            user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        }
        return userRepository.save(user);
    }

    // loginController
    public SignupResponseDto signup(LoginRequestDto signupRequest) {
        User user = signupInternal(signupRequest, AuthProviderType.EMAIL, null);
        return new SignupResponseDto(user.getId(), user.getUsername());
    }


    @Transactional
    public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
        // fetch providerType and provider id
        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtil.getProviderIdFromOAuth2User(oAuth2User, registrationId);
        // save providerType and provider id info with user
        User user = userRepository.findByProviderIdAndProviderType(providerId, providerType).orElse(null);

        String email = oAuth2User.getAttribute("email");
        User emailUser = userRepository.findByUsername(email).orElse(null);

        // if user has account directly login
        if (user == null && emailUser == null) {
            // signup
            String username = authUtil.getUsernameFromOAuth2Provider(oAuth2User, registrationId, providerId);
            user = signupInternal(new LoginRequestDto(username, null), providerType, providerId);

        } else if (user != null) {
            // otherwise first signup then login
            if (email != null && !email.isBlank() && !email.equals(user.getUsername())) {
                user.setUsername(email);
                userRepository.save(user);
            }
        } else {
            throw new BadCredentialsException("This email is already registered with provider: " +
                    emailUser.getProviderType());
        }

        LoginResponseDto loginResponseDto = new LoginResponseDto(authUtil.generateAccessToken(user), user.getId());

        return ResponseEntity.ok(loginResponseDto);
    }
}