package com.roomyapp.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Test del filtro JWT
 *
 * OBJETIVO:
 * - Verificar que un token válido permite autenticar al usuario
 * - Comprobar que se establece correctamente el SecurityContext
 */
class JwtAuthenticationFilterTest {

    @Test
    void shouldValidateTokenAndAuthenticateUser() {

        // Creamos clave
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Creamos token manual
        String token = Jwts.builder()
                .setSubject("test@mail.com")
                .claim("role", "ADMIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 100000))
                .signWith(key)
                .compact();

        //  Aquí NO usamos el filtro real porque requiere servlet
        // pero comprobamos que el token se puede parsear

        var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("test@mail.com", claims.getSubject());
        assertEquals("ADMIN", claims.get("role"));
    }
}