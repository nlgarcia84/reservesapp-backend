package com.roomyapp.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/*
Clase que genera un token para ser utlizado en el login y en el registro para poder navegar por la web sin solicitar
contrasenya continuamente y como una forma de garantizar la autenticación del usuario
 */
@Component
public class JwtUtil {

    // Lee la clave de las propiedades o variable de entorno JWT_SECRET
    @Value("${jwt.secret}")
    private String secretKey;

    //@Value("${jwt.expiration:604800000}")
    //private long expiration;

    //Expiracion normal
    @Value("${jwt.expiration}")
    private long normalExpiration;

    //Expiracion rememberMe
    @Value ("${jwt.remember.expiration}")
    private long rememberExpiration;

    private Key key;

    // Inicializar la clave después de inyectar las propiedades (sincronizado para evitar race conditions)
    private synchronized void initKey() {
        if (key == null && secretKey != null) {
            this.key = Keys.hmacShaKeyFor(
                Base64.getEncoder().encode(secretKey.getBytes())
            );
            System.out.println("JwtUtil: Clave inicializada con secretKey de longitud: " + secretKey.length());
        }
    }

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
        System.out.println("Token generado (" + (rememberMe ? "REMEMBER ME" : "NORMAL") + ") para: " + email);
        return token;
    }

    // Metodo para obtener la clave (lo usará el filtro JWT)
    public Key getKey() {
        initKey();
        if (key == null) {
            throw new RuntimeException("No se pudo inicializar la clave JWT. Verifique que jwt.secret está configurado.");
        }
        return key;
    }

    public String getSecretKey() {
        return secretKey;
    }
}

