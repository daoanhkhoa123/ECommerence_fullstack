package com.example.backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.backend.repository.AccountRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final AccountRepository accountRepository;

    private static final String SECRET_KEY =
        "anAbyssInMyOwnanAbyssInMyOwnanAbyssInMyOwnanAbyssInMyOwnanAbyssInMyOwn";

    // Define expiration time in milliseconds (1 hour)
    private static final long EXPIRATION_TIME = 3600000; 

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    /** 🧭 Returns the configured token lifetime in seconds */
    public long getExpiration() {
        return EXPIRATION_TIME / 1000; // convert ms → seconds
    }

    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // or throw an exception
        }

        String username;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            username = principal.toString();
        }

        var account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return account.getId();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        System.out.printf("🔍 Token username=%s, DB username=%s \n", 
        username, userDetails.getUsername());

        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
}
