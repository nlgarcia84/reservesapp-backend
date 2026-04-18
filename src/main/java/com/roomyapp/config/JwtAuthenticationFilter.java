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


/**
 * Filtro de autenticación basado en JWT.
 *
 * Este filtro se ejecuta en cada petición HTTP y se encarga de:
 * - Leer el token JWT desde la cabecera Authorization
 * - Validar el token
 * - Extraer el email y el rol del usuario
 * - Crear la autenticación de Spring Security
 * - Guardar el usuario autenticado en el SecurityContext
 *
 * Permite que la aplicación funcione sin sesiones (stateless).
 */

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;

    /**
     * Constructor con inyección de JwtUtil.
     *
     * @param jwtUtil utilidad para trabajar con tokens JWT
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Metodo principal del filtro que se ejecuta en cada request.
     *
     * Flujo:
     * - Permite peticiones OPTIONS (CORS)
     * - Permite endpoints públicos (/auth)
     * - Extrae y valida el token JWT
     * - Crea la autenticación del usuario
     * - Guarda el usuario en el contexto de seguridad
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @param filterChain cadena de filtros
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        /**
         * Permite peticiones OPTIONS necesarias para CORS (preflight).
         */
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getServletPath();

        /**
         * Permite endpoints públicos sin necesidad de token (login y register).
         */
        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        /**
         * Obtiene la cabecera Authorization.
         */
        String authHeader = request.getHeader("Authorization");

        logger.info("[JWT FILTER] Request: {}", path);

        try {
            /**
             * Comprueba que existe token y que empieza por "Bearer ".
             */
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                /**
                 * Extrae el token eliminando el prefijo "Bearer ".
                 */
                String token = authHeader.substring(7);

                logger.info("Token recibido (parcial): {}...",
                        token.substring(0, Math.min(20, token.length())));

                /**
                 * Valida el token y obtiene los datos (claims).
                 */
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtUtil.getKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                /**
                 * Extrae el email y el rol del usuario.
                 */
                String email = claims.getSubject();
                String role = (String) claims.get("role");

                logger.info("Usuario autenticado: {} con rol {}", email, role);

                /**
                 * Crea la lista de roles para Spring Security.
                 */
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }

                /**
                 * Crea el objeto de autenticación.
                 */
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                /**
                 * Guarda el usuario autenticado en el contexto de seguridad.
                 */
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("Usuario guardado en SecurityContext: {}", email);

            }

        } catch (Exception e) {
            /**
             * Si el token es inválido o ha expirado, no autentica al usuario.
             */
            logger.warn("Token inválido o expirado: {}", e.getMessage());
        }

        /**
         * Continúa la cadena de filtros.
         */
        filterChain.doFilter(request, response);
    }
}