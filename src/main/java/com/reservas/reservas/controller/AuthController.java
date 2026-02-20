package com.reservas.reservas.controller;

import com.reservas.reservas.dto.LoginRequest;
import com.reservas.reservas.entity.User;
import com.reservas.reservas.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:3000")
public class AuthController {

    private final UserService userService;

    //Constructor con inyección de dependencia de servicio de usuario
    public AuthController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request){
        return userService.login(request.getEmail(), request.getPassword());
    }
}
