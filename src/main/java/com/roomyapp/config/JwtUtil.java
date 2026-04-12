package com.roomyapp.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 🔐 CLAVE SECRETA (DEBE SER FUERTE EN PRODUCCIÓN)
    private final String SECRET = "mySuperSecretKeyForJwtGenerationThatShouldBeLongEnough123456";

    // ⏳ Tiempo de expiración normal (24 horas)
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // ⏳ Tiempo de expiración extendido para rememberMe (7 días)
    private final long EXTENDED_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

    // 🔑 Obtener clave para firmar tokens
    public Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // 🔐 Generar token JWT
    public String generateToken(String email, String role, boolean rememberMe) {
        long expirationTime = rememberMe ? EXTENDED_EXPIRATION_TIME : EXPIRATION_TIME;
        
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔍 Extraer claims
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 👤 Extraer email
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // 🔐 Validar token
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
