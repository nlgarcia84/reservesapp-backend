package com.roomyapp.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.slf4j.LoggerFactory;
/*
 * Clase utilitaria para la generación y gestión de tokens JWT.
 *
 * Responsabilidades:
 * - Generar tokens JWT tras autenticación (login / register)
 * - Incluir información relevante del usuario (email, rol, rememberMe)
 * - Configurar expiración dinámica según rememberMe
 * - Proporcionar la clave de firma para validación del token
 *
 * Seguridad:
 * - Usa algoritmo HS256
 * - La clave secreta se obtiene desde application.properties
 *
 * Uso:
 * - AuthController → genera token
 * - JwtAuthenticationFilter → valida token
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    // Lee la clave de las propiedades o variable de entorno JWT_SECRET desde el applicatio.properties
    @Value("${jwt.secret}")
    private String secretKey;

    //Expiracion normal
    @Value("${jwt.expiration}")
    private long normalExpiration;

    //Expiracion rememberMe
    @Value ("${jwt.remember.expiration}")
    private long rememberExpiration;

    private Key key;

    // Inicializa la clave de firma de forma segura (lazy initialization + thread-safe)
    // - Lazy: solo se crea cuando se necesita
    // - Thread-safe: synchronized evita problemas en concurrencia
    // - secretKey es la clave única y secreta del Backend con la que firmará las tokens que generará,
    private synchronized void initKey() {
        if (key == null && secretKey != null) {
            this.key = Keys.hmacShaKeyFor(
                Base64.getEncoder().encode(secretKey.getBytes())
            );
        }
    }

    // Genera un token JWT con:
    // - subject: email del usuario
    // - claim: rol del usuario
    // - claim: rememberMe (para control de sesión)
    // - expiración dinámica según rememberMe (corta o larga)
    // - firma el token con su clave (key)
    public String generateToken(String email, String role, boolean rememberMe){
        initKey();

        long expirationToUse = rememberMe ? rememberExpiration: normalExpiration;

        String token = Jwts.builder()
                .setSubject(email)
                .claim("role",role)
                .claim("rememberMe", rememberMe) //
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationToUse))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        logger.info("Token generado de tipo (" + (rememberMe ? "REMEMBER ME" : "NORMAL") + ") para: " + email);
        return token;
    }

    // Devuelve la clave secreta utilizada para firmar y validar tokens JWT
    // Este metodo es utilizado por el filtro de seguridad (JwtAuthenticationFilter)
    // para verificar la autenticidad de los tokens recibidos en cada petición
    public Key getKey() {
        initKey();
        if (key == null) {
            throw new RuntimeException("No se pudo inicializar la clave JWT. Verifique que jwt.secret está configurado.");
        }
        return key;
    }
/*
    public String getSecretKey() {
        return secretKey;
    }

 */
}

