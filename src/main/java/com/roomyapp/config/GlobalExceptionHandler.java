package com.roomyapp.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Manejar excepciones de credenciales incorrectas o usuario no encontrado
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex,
            WebRequest request) {

        String message = ex.getMessage();
        logger.error("RuntimeException capturada: " + message, ex);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // Si es un error de autenticación (usuario no encontrado, credenciales incorrectas)
        if (message != null && (message.contains("Usuario no encontrado") || 
                               message.contains("Credenciales incorrectas") ||
                               message.contains("El usuario ya existe"))) {
            body.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        // Por defecto, error interno del servidor
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Manejar otras excepciones
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(
            Exception ex,
            WebRequest request) {

        logger.error("Excepción capturada: " + ex.getMessage(), ex);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
