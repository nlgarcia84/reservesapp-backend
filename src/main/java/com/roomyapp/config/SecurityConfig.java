package com.roomyapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de seguridad de la aplicación.
 *
 * Define:
 * - Codificación de contraseñas (BCrypt)
 * - Reglas de acceso a endpoints (autorización)
 * - Uso de JWT (sin sesiones)
 * - Configuración CORS para permitir peticiones desde el frontend
 *
 * Es el núcleo de Spring Security en la aplicación.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Bean para encriptar contraseñas.
     *
     * Utiliza BCrypt, que es el estándar recomendado en seguridad.
     *
     * @return instancia de PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración principal de seguridad HTTP.
     *
     * Define:
     * - Qué endpoints son públicos
     * - Qué endpoints requieren autenticación
     * - Uso de JWT en lugar de sesiones
     * - Integración del filtro JWT en la cadena de seguridad
     *
     * @param http configuración de seguridad HTTP de Spring
     * @param jwtUtil utilidad para gestionar tokens JWT
     * @return SecurityFilterChain configurado
     * @throws Exception en caso de error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        /**
         * Instancia del filtro JWT que se ejecutará en cada petición.
         */
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil);

        http
            .csrf().disable()//Desactiva CSRF ya que usamos JWT (stateless).
            .cors() //Habilita CORS para permitir peticiones desde el frontend.
            .and()
            .authorizeHttpRequests()// Configuración de autorización de endpoints.
                .requestMatchers(HttpMethod.OPTIONS).permitAll()//Permite peticiones OPTIONS (necesarias para CORS).
                .requestMatchers("/auth/login", "/auth/register").permitAll()//Endpoints públicos (no requieren autenticación).
                .requestMatchers("/health", "/health/**").permitAll()//Endpoint de salud (útil para deploy/monitorización).
                .anyRequest().authenticated()//Cualquier otro endpoint requiere autenticación
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//Configura la aplicación como stateless (sin sesiones).
                                                                                      //Cada petición debe incluir su token JWT.
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Añade el filtro JWT antes del filtro de autenticación estándar.

        return http.build();
    }

    /**
     * Configuración de CORS (Cross-Origin Resource Sharing).
     *
     * Permite que el frontend (por ejemplo React en localhost:3000)
     * pueda comunicarse con el backend.
     *
     * Define:
     * - Orígenes permitidos
     * - Métodos HTTP permitidos
     * - Cabeceras permitidas
     * - Cabeceras expuestas (Authorization)
     *
     * @return configuración CORS
     */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        /**
         * Permite cualquier origen (en producción debería restringirse).
         */
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        /**
         * Métodos HTTP permitidos.
         */
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        /**
         * Cabeceras permitidas.
         */
        configuration.setAllowedHeaders(Arrays.asList("*"));

        /**
         * Cabeceras que el cliente puede leer.
         */
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        /**
        * Permite envío de credenciales (cookies/token).
        */
        configuration.setAllowCredentials(true);

        /**
         * Tiempo en caché de la configuración CORS.
         */
        configuration.setMaxAge(3600L);

        /**
         * Aplica esta configuración a todas las rutas.
         */
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}