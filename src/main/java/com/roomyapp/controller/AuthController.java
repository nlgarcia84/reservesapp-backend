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

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:3000")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final JwtUtil jwtUtil;

    //Constructor con inyección de dependencia de servicio de usuario
    public AuthController(UserService userService, JwtUtil jwtUtil){
        this.userService=userService;
        this.jwtUtil=jwtUtil;
    }

    //Convierte la petición de React (JSON) en objeto LoginRequest
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        logger.info("Recibido login request para email: " + request.getEmail());
        try {
            //Este controller llama al servicio de User (userService)
            User user= userService.login(
                    request.getEmail(),
                    request.getPassword()
            );

            String token= jwtUtil.generateToken(
                    user.getEmail(),
                    user.getRole().name(),
                    request.getRememberMe() //login necesita saber si tiene que recordar la pass
            );
            logger.info("Se ha solicitado rememberMe: de tipo " + request.getRememberMe());
            logger.info("Login exitoso para usuario: " + user.getEmail());
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

    //Register
    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request){
        logger.info("Recibido register request para email: " + request.getEmail());
        try {
            User user= userService.register(
                    request.getName(),
                    request.getEmail(),
                    request.getPassword()
            );

            String token = jwtUtil.generateToken(
                    user.getEmail(),
                    user.getRole().name(),
                    false //El registro no necesita de un jwt para recordar
            );
            logger.info("Registro exitoso para usuario: " + user.getEmail());
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
