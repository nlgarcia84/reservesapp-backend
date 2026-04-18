package com.roomyapp.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Clase utilitaria para la gestión de tokens JWT.
 *
 * Se encarga de:
 * - Generar tokens JWT al hacer login o registro
 * - Firmar los tokens con una clave secreta
 * - Extraer información (claims) del token
 * - Validar si un token es correcto o ha expirado
 *
 * Esta clase es utilizada por el controlador de autenticación
 * y por el filtro JWT para validar peticiones.
 */
@Component
public class JwtUtil {

    /**
     * Clave secreta utilizada para firmar los tokens.
     * En producción debe ser segura, larga y almacenarse en variables de entorno.
     */
    private final String SECRET = "mySuperSecretKeyForJwtGenerationThatShouldBeLongEnough123456";

    /**
     * Tiempo de expiración estándar del token (24 horas).
     */
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    /**
     * Tiempo de expiración extendido (7 días) para la opción rememberMe.
     */
    private final long EXTENDED_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

    /**
     * Genera la clave criptográfica a partir del SECRET.
     *
     * @return clave utilizada para firmar y validar tokens JWT
     */
    public Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Genera un token JWT con la información del usuario.
     *
     * @param email identificador del usuario (subject del token)
     * @param role rol del usuario (ADMIN o EMPLOYEE)
     * @param rememberMe indica si se debe generar un token con mayor duración
     * @return token JWT firmado
     */
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

    /**
     * Extrae todos los datos (claims) del token.
     *
     * @param token token JWT
     * @return claims contenidos en el token
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrae el email (subject) del token.
     *
     * @param token token JWT
     * @return email del usuario
     */
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Valida si un token es correcto.
     *
     * Comprueba:
     * - Firma válida
     * - Token no expirado
     *
     * @param token token JWT
     * @return true si es válido, false si es inválido o ha expirado
     */
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
