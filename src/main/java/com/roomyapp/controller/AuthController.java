package com.roomyapp.controller;

import com.roomyapp.config.JwtUtil;
import com.roomyapp.dto.LoginRequest;
import com.roomyapp.dto.LoginResponse;
import com.roomyapp.dto.RegisterRequest;
import com.roomyapp.entity.User;
import com.roomyapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Controlador de autenticación de la aplicación.
 *
 * RESPONSABILIDADES:
 * - Gestionar el login de usuarios
 * - Gestionar el registro de nuevos usuarios
 * - Generar tokens JWT tras autenticación
 *
 * FLUJO:
 * - Recibe peticiones del frontend (React)
 * - Delega la lógica al UserService
 * - Genera un JWT con JwtUtil
 * - Devuelve la información del usuario + token al frontend
 *
 * IMPORTANTE:
 * - Los endpoints de este controlador son públicos (no requieren token)
 * - El token se utiliza en peticiones posteriores, no en login/register
 */

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final JwtUtil jwtUtil;

    //Constructor con inyección de dependencia de servicio de usuario
    public AuthController(UserService userService, JwtUtil jwtUtil){
        this.userService=userService;
        this.jwtUtil=jwtUtil;
    }

    // Endpoint de autenticación de usuario (login)
    //
    // Recibe credenciales (email y password) desde el frontend
    // Valida el usuario mediante UserService
    // Genera un token JWT con información del usuario y expiración según rememberMe
    // Devuelve los datos del usuario junto con el token
    // rememberMe:
    // - true → token con expiración larga
    // - false → token con expiración normal
    //Convierte la petición de React (JSON) en objeto LoginRequest
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        logger.info("Recibido login request para email: " + request.getEmail());
        try {
            //Este controller llama al servicio de User (userService)
            // Llamada al servicio para validar credenciales
            // Si el usuario no existe o la contraseña es incorrecta → lanza excepción
            User user= userService.login(
                    request.getEmail(),
                    request.getPassword()
            );

            // Generación del token JWT
            // Incluye email, rol y configuración de expiración según rememberMe
            String token= jwtUtil.generateToken(
                    user.getEmail(),
                    user.getRole().name(),
                    request.getRememberMe(), //login necesita saber si tiene que recordar la pass
                    user.getId() //NECESARIO PARA RESERVAS
            );
            logger.info("Login exitoso para usuario " + user.getName() +", con email "+ user.getEmail());

            // Logs informativos sobre el tipo de sesión (normal o persistente)
            if(request.getRememberMe()){
                logger.info("El usuario ha solicitado recordar su contraseña (rememberMe: " + request.getRememberMe() + ")");
            } else{
                logger.info("El usuario no ha solicitado recordar su contraseña (rememberMe: " + request.getRememberMe() + ")");
            }

            return new LoginResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().name(), //.name() convierte a String y el frontend recibe ADMIN o EMPLOYEE, porque Role es enum
                    token
            );
        } catch (Exception e) {
            logger.error("Error en login: " + e.getMessage(), e);
            throw e;
        }
    }

    // Endpoint de registro de nuevos usuarios
    //
    // Recibe datos del usuario desde el frontend
    // Crea el usuario en base de datos (con contraseña encriptada)
    // Genera un token JWT automáticamente tras el registro
    // Devuelve los datos del usuario junto con el token
    //
    // NOTA:
    // - rememberMe no aplica en registro → se usa expiración normal
    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request){
        logger.info("Recibido register request para usuario nuevo : "+ request.getName() + " ,con email " + request.getEmail());
        try {
            // Llamada al servicio para crear el usuario
            // Incluye validación de usuario existente y encriptación de contraseña
            User user= userService.register(
                    request.getName(),
                    request.getEmail(),
                    request.getPassword()
            );

            // Generación del token JWT tras registro exitoso
            String token = jwtUtil.generateToken(
                    user.getEmail(),
                    user.getRole().name(),
                    false, //El registro no necesita de un jwt para recordar
                    user.getId() //NECESARIO PARA RESERVAS
            );
            //Confirmación en log - terminal de registro exitoso
            logger.info("Registro exitoso para usuario: " + user.getName() + " ,con email " +user.getEmail());
            return new LoginResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().name(), //.name() convierte a String y el frontend recibe ADMIN o EMPLOYEE, porque Role es enum
                    token
            );
        } catch (Exception e) {
            logger.error("Error en register: " + e.getMessage(), e);
            throw e;
        }
    }

}
