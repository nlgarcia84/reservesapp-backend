package com.roomyapp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // ✅ FIX 1: permitir preflight CORS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getServletPath();

        // ✅ FIX 2: permitir endpoints públicos
        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        logger.info("[JWT FILTER] Request: {}", path);

        try {

            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                String token = authHeader.substring(7);

                logger.info("Token recibido (parcial): {}...",
                        token.substring(0, Math.min(20, token.length())));

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtUtil.getKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();
                String role = (String) claims.get("role");

                logger.info("Usuario autenticado: {} con rol {}", email, role);

                List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("Usuario guardado en SecurityContext: {}", email);

            }

        } catch (Exception e) {
            logger.warn("Token inválido o expirado: {}", e.getMessage());
        }

        // ✅ SIEMPRE CONTINUAR CADENA
        filterChain.doFilter(request, response);
    }
}