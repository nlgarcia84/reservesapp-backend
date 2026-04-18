package com.roomyapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Configuración global de CORS (Cross-Origin Resource Sharing).
 *
 * Permite que el frontend (por ejemplo React en localhost o en Vercel)
 * pueda comunicarse con el backend sin que el navegador bloquee
 * las peticiones por políticas de seguridad de origen cruzado.
 *
 * Define:
 * - Qué orígenes pueden acceder al backend
 * - Qué métodos HTTP están permitidos
 * - Qué cabeceras se pueden enviar
 * - Qué cabeceras se exponen al cliente (Authorization)
 * - Si se permiten credenciales (cookies o tokens)
 *
 * Es necesaria cuando frontend y backend están en distintos dominios o puertos.
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura las reglas de CORS para toda la aplicación.
     *
     * Aplica la configuración a todas las rutas (/**) y permite
     * la comunicación con los orígenes especificados.
     *
     * @param registry registro de configuraciones CORS de Spring
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")

                /**
                 * Orígenes permitidos (frontend en desarrollo y producción).
                 */
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:3001",
                        "https://reservesapp-frontend-5clg.vercel.app"
                )
                /**
                 * Métodos HTTP permitidos.
                 */
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                /**
                 * Cabeceras permitidas en la petición.
                 */
                .allowedHeaders("*")
                /**
                 * Cabeceras que el cliente puede leer (ej: Authorization).
                 */
                .exposedHeaders("Authorization")
                /**
                 * Permite enviar credenciales (tokens, cookies).
                 */
                .allowCredentials(true)
                /**
                 * Tiempo en segundos que el navegador cachea la configuración CORS.
                 */
                .maxAge(3600);
    }
}