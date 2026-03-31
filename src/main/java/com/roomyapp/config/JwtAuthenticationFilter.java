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

// Filtro que valida el token JWT en cada solicitud
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        logger.info("Procesando petición a: " + request.getRequestURI());
        logger.info("Authorization header: " + authHeader);

        try {
            // Solo procesar si hay un token Bearer
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                logger.info("Token recibido: " + token.substring(0, Math.min(20, token.length())) + "...");

                try {
                    // Validar y obtener los claims del token
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(jwtUtil.getKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    String email = claims.getSubject();
                    String role = (String) claims.get("role");

                    logger.info("Token válido para usuario: " + email + " con rol: " + role);

                    // Crear autoridades basadas en el rol
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    if (role != null) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    }

                    // Crear el token de autenticación
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);

                    // Establecer en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.info("Autenticación establecida en SecurityContext para: " + email);
                } catch (Exception e) {
                    logger.error("Error al validar token JWT: " + e.getMessage(), e);
                    logger.info("Continuando sin autenticación para esta petición");
                    // No hacemos nada, permitimos que continúe sin autenticación
                    // Las rutas protegidas rechazarán la petición en el siguiente paso
                }
            } else {
                logger.info("No se encontró token Bearer. Es una petición pública.");
            }
        } catch (Exception e) {
            logger.error("Error inesperado en JwtAuthenticationFilter: " + e.getMessage(), e);
            // Continuamos de todas formas
        }

        // IMPORTANTE: Siempre pasar al siguiente filtro, nunca bloquear aquí
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Error al pasar al siguiente filtro: " + e.getMessage(), e);
            throw e;
        }
    }
}
