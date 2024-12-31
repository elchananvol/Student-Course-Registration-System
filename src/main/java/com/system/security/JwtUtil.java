package com.system.security;

import com.system.model.LoginResponse;
import com.system.util.Exceptions;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "your-very-secure-secret-key"; // Replace with secure key

    // Token validity in milliseconds (e.g., 1 hour)
    private final long VALIDITY = 3600000;

    public String generateToken(String email, LoginResponse.RoleEnum role, Integer studentId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        if (studentId != null) {
            claims.put("studentId", studentId);
        }
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Claims extractClaims(String token) throws ExpiredJwtException {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            // Token expired
            return false;
        } catch (Exception e) {
            // Invalid token
            return false;
        }
    }

}