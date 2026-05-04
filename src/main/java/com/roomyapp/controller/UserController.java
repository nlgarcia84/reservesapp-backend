package com.roomyapp.controller;

import com.roomyapp.entity.User;
import com.roomyapp.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Controlador per a la gestió de usuaris
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Crear nou usuari
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Esborrar usuari per ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // Obtenir tots els usuaris
    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    // Endpoint per obtenir el recompte d'usuaris actius
    // El client crida aquest endpoint cada 15s per mantenir la sessió
    @GetMapping("/online")
    public Map<String, Object> getOnlineUsers(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticat");
        }

        Long userId = ((Number) authentication.getPrincipal()).longValue();
        System.out.println("🔵 [/users/online] userId: " + userId + " | timestamp: " + System.currentTimeMillis());

        userService.markUserAsActive(userId);
        long count = userService.getOnlineUsersCount();
        System.out.println("🔵 [/users/online] count: " + count + " | activeUsers: " + userService.getActiveUsersMap().keySet());

        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        response.put("updatedAt", Instant.now().toString());
        return response;
    }

}