package com.example.backend.application;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.backend.dto.AuthencationRequest;
import com.example.backend.dto.AuthencationRespond;
import com.example.backend.service.JwtService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthencationApplication {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthencationRespond login(AuthencationRequest request)
    {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.emailUsername(), request.password()));

        String token =  jwtService.generateToken((UserDetails) authentication.getPrincipal());
        return new AuthencationRespond(token);
    }
}
