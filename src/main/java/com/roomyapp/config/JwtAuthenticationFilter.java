package com.roomyapp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Filtro de seguridad que se ejecuta en CADA petición HTTP, para que el Backend sepa quién realmente
 * hace cada petición (Admin o Employee). Recibe el token JWT que le envía el Front.
 *
 * RESPONSABILIDAD PRINCIPAL:
 * - Leer el token JWT del header Authorization
 * - Validar el token ( firma + expiración)
 * - Extraer información del usuario (email y rol)
 * - Registrar al usuario como autenticado en Spring Security (Usuario autenticado)
 *
 * IMPORTANTE:
 * - NO bloquea la petición directamente
 * - Si el token es inválido o no existe, deja pasar la petición
 *   y será Spring Security quien decida si permitir o no el acceso
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Obtener el header Authorization de la petición HTTP
        // Aquí el frontend debería enviar el token en formato: Authorization: Bearer <token>
        String authHeader = request.getHeader("Authorization");
        logger.info(" [JWT FILTER] Procesando petición del header Authorization: {}", request.getRequestURI());

        try {
            // 2. Comprobar si existe un token Bearer en la petición
            // Si no existe, significa que:
            // - Es login/register, o
            // - Es un endpoint público, o
            // - El frontend no envió el token
            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                // 3. Extraer el token eliminando el prefijo "Bearer "
                String token = authHeader.substring(7);

                // Se muestra solo una parte del token por seguridad
                logger.info("Token recibido (parcial): {}...",
                        token.substring(0, Math.min(20, token.length())));

                // En este punto aún NO sabemos quién es el usuario
                // (el token todavía no ha sido validado ni leído)
                logger.info("Aún se desconoce al usuario. Token aún no ha sido desglosado.");

                try {
                    // 4. Validar el token JWT
                    // - Verifica firma con la clave secreta
                    // - Verifica expiración
                    // - Verifica integridad
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(jwtUtil.getKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    // 5. Extraer información del token (claims)
                    // Aquí ya sabemos quién es el usuario
                    String email = claims.getSubject();
                    String role = (String) claims.get("role");

                    logger.info("Token válido para usuario: " + email + " con rol: " + role);

                    // 6. Convertir el rol en autoridad de Spring Security
                    // Spring requiere el prefijo ROLE_
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    if (role != null) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    }

                    // 7. Crear el objeto de autenticación
                    // Representa al usuario autenticado dentro del sistema
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);

                    // 8. Guardar la autenticación en el contexto de seguridad
                    // A partir de aquí, Spring sabe quién es el usuario
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.info("Usuario autenticado en SecurityContext: {}", email);

                } catch (Exception e) {
                    // Si el token es inválido, expirado o manipulado
                    logger.warn("Token inválido o expirado: {}", e.getMessage());

                    // No bloqueamos aquí → dejamos que Spring Security actúe después
                    logger.info("Continuando petición sin autenticación");
                }

            } else {
                // No hay token en la petición
                // Caso normal en:
                // - /auth/login
                // - /auth/register
                // - endpoints públicos
                logger.info("Petición sin token JWT (posible endpoint público o login)");
            }
        } catch (Exception e) {
            // Error inesperado en el filtro (no relacionado con JWT directamente)
            logger.error("Error inesperado en JwtAuthenticationFilter: {}", e.getMessage(), e);
        }

        // 9. Continuar con la cadena de filtros (OBLIGATORIO)
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Error al pasar al siguiente filtro: " + e.getMessage(), e);
            throw e;
        }
    }
}
