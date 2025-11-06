package com.example.backend.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.AuthencationRequest;
import com.example.backend.dto.AuthencationRespond;
import com.example.backend.security.JwtService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthencationApplication {
    private static final Logger log = LoggerFactory.getLogger(AuthencationApplication.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthencationRespond login(AuthencationRequest request) {
        log.info("Attempting login for user: {}", request.emailUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.emailUsername(), request.password()
                )
            );

            UserDetails user = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(user);
            long expiresIn = jwtService.getExpiration();

            log.info("User {} authenticated successfully", user.getUsername());

            return new AuthencationRespond(
                token,
                "Bearer",
                expiresIn,
                user.getUsername(),
                user.getAuthorities().stream()
                    .findFirst()
                    .map(auth -> auth.getAuthority())
                    .orElse("USER")
            );

        } catch (AuthenticationException ex) {
            log.warn("Failed login attempt for user: {}", request.emailUsername());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }
}
