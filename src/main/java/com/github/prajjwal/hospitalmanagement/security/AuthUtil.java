package com.github.prajjwal.hospitalmanagement.security;

import com.github.prajjwal.hospitalmanagement.model.User;
import com.github.prajjwal.hospitalmanagement.model.type.AuthProviderType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Component
@Log4j2
public class AuthUtil {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    public SecretKey getEncodedSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getEncodedSecretKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getEncodedSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public AuthProviderType getProviderTypeFromRegistrationId(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "facebook" -> AuthProviderType.FACEBOOK;
            case "google" -> AuthProviderType.GOOGLE;
            case "twitter" -> AuthProviderType.TWITTER;
            case "github" -> AuthProviderType.GITHUB;
            case "email" -> AuthProviderType.EMAIL;
            default -> throw new IllegalArgumentException("Unsupported OAuth provider");
        };
    }

    public String getProviderIdFromOAuth2User(OAuth2User oAuth2User, String registrationId) {
        String providerId = switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> Objects.requireNonNull(oAuth2User.getAttribute("id")).toString();
            default -> {

                log.error("Unsupported OAuth provider {}",  registrationId);
                throw new IllegalStateException("Unsupported OAuth2 provider " + registrationId);
            }
        };
        if (providerId == null || providerId.isBlank()) {
            log.error("Unable to determine providerId for provider {}", registrationId);
            throw new IllegalStateException("Unable to determine providerId for OAuth2 login");
        }
        return providerId;
    }

    public String getUsernameFromOAuth2Provider(OAuth2User oAuth2User, String registrationId, String providerId) {
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