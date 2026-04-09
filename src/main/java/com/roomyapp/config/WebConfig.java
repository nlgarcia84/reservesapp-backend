package com.roomyapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/*
 * Configuración global de CORS (Cross-Origin Resource Sharing) para la aplicación.
 *
 * Permite que el frontend (por ejemplo, React en http://localhost:3000)
 * pueda comunicarse con el backend Spring Boot sin que el navegador bloquee
 * las peticiones por políticas de seguridad de origen cruzado.
 *
 * Configuración aplicada:
 * - Se permite acceso a todas las rutas (/**)
 * - Se autorizan los métodos HTTP principales (GET, POST, PUT, DELETE, OPTIONS)
 * - Se aceptan todos los headers
 * - Se permite el envío de credenciales (como tokens en Authorization)
 *
 * Es necesario cuando frontend y backend se ejecutan en dominios/puertos distintos.
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "https://reservesapp-frontend.vercel.app",
                        "https://reservesapp-frontend-*.vercel.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}