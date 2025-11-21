package com.github.prajjwal.hospitalmanagement.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class WebSecurityConfig {

    private final JwtAuthenticationFiler authenticationFiler;
    private final OAuth2SuccessHandler  oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/public/**", "/auth/**").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/patients/**").hasAnyRole("PATIENT", "ADMIN")
//                        .requestMatchers("/doctors/**").hasAnyRole("ADMIN", "DOCTOR"))
                        .anyRequest().authenticated())
                .addFilterBefore(authenticationFiler, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .failureHandler(
                                (request, response, exception) -> {
                                log.error("OAuth2 error: {}", exception.getMessage());
                        })
                        .successHandler(oAuth2SuccessHandler)
                );

        return http.build();
    }
}