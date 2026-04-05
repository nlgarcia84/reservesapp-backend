package com.roomyapp.controller;

import com.roomyapp.entity.User;
import com.roomyapp.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/*
 * Controlador encargado de la gestión de usuarios.
 *
 * RESPONSABILIDADES:
 * - Crear nuevos usuarios (desde panel admin)
 * - Eliminar usuarios existentes
 * - Obtener la lista de usuarios
 *
 * FLUJO:
 * - Recibe peticiones del frontend
 * - Delega la lógica al UserService
 * - Devuelve los datos al frontend
 *
 * IMPORTANTE:
 * - Estos endpoints deberían estar protegidos (ej: solo ADMIN)
 * - El control de acceso se realiza mediante JWT + Spring Security
 */


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint para crear un nuevo usuario
    //
    // Recibe un objeto User desde el frontend
    // Delega la creación al UserService:
    // - Valida si el usuario ya existe
    // - Encripta la contraseña (BCrypt)
    // - Asigna rol por defecto (EMPLOYEE)
    //
    // Uso típico:
    // - Creación de usuarios desde un panel de administración
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Endpoint para eliminar un usuario por su ID
    //
    // Recibe el ID del usuario como parámetro en la URL
    // Delega la eliminación al UserService:
    // - Verifica si el usuario existe
    // - Elimina el usuario de la base de datos
    //
    // Uso típico:
    // - Gestión de usuarios por parte de un administrador
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // Endpoint para obtener todos los usuarios
    //
    // Devuelve una lista completa de usuarios almacenados en la base de datos
    //
    // Uso típico:
    // - Mostrar listado de usuarios en el frontend (panel admin)
    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }
}
