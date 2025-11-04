package com.github.prajjwal.hospitalmanagement.securtiy;

import com.github.prajjwal.hospitalmanagement.model.User;
import com.github.prajjwal.hospitalmanagement.model.type.AuthProviderType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class AuthUtil {

    @Value("${jwt.secretKey}")
    private String jwtSecurityKey;

    private SecretKey getSecurityKey() {
        return Keys.hmacShaKeyFor(jwtSecurityKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getSecurityKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecurityKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();

    }

    public AuthProviderType getAuthProviderTypeFromRegistrationId(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> AuthProviderType.GOOGLE;
            case "facebook" -> AuthProviderType.FACEBOOK;
            case "github" -> AuthProviderType.GITHUB;
            default -> throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
        };
    }

    private String getProviderIdFromOAuth2User(OAuth2User oAuth2User, String registrationId) {
        String providerId = switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("id");
            default -> {
                log.error("Unsupported OAuth2 provider: {}", registrationId);
                throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
            }
        };
        if (providerId == null || providerId.isBlank()) {
            log.error("Unable to find providerId for provider: {}", registrationId);
            throw new IllegalArgumentException("Unable to find providerId for OAuth2 login");
        }
        return providerId;
    }

    private String getUsernameFromOAuth2User(OAuth2User oAuth2User, String registrationId, String providerId) {
        String email = oAuth2User.getAttribute("email");
        if (email != null && !email.isBlank()) {
            return email;
        }
        return switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("login");
            default -> providerId;
        };
    }
}