package com.roomyapp.service;

import com.roomyapp.dto.RegisterRequest;
import com.roomyapp.entity.User;
import com.roomyapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
 * Servicio que gestiona la lógica de negocio relacionada con usuarios.
 *
 * RESPONSABILIDADES:
 * - Autenticación de usuarios (login)
 * - Registro de nuevos usuarios
 * - Gestión de usuarios (crear, eliminar, listar)
 *
 * FLUJO:
 * - Recibe peticiones desde el controlador
 * - Interactúa con el repositorio para acceder a la BD
 * - Aplica reglas de negocio (validaciones, encriptación)
 *
 * SEGURIDAD:
 * - Las contraseñas se encriptan con BCrypt antes de guardarse
 * - Nunca se comparan contraseñas en texto plano
 *
 * IMPORTANTE:
 * - Aquí reside la lógica principal de la aplicación (no en controllers)
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    //Constructor con inyección de dependencia de Repositorio
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Metodo de autenticación de usuario
    //
    // - Busca el usuario por email
    // - Si no existe → lanza excepción
    // - Compara contraseña introducida con la encriptada en BD
    // - Si no coincide → lanza excepción
    // - Si todo es correcto → devuelve el usuario
    public User login(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }
        return user;
    }

    // Metodo de registro de usuario
    //
    // - Verifica si el email ya existe
    // - Crea nuevo usuario
    // - Encripta la contraseña con BCrypt
    // - Asigna rol por defecto (EMPLOYEE)
    // - Guarda el usuario en la base de datos
    public User register(String name, String email, String rawPassword){
        //Comprobamos si el usuario existe
        if(userRepository.findByEmail(email).isPresent()){
            throw  new RuntimeException("El usuario ya existe");
        }

        //Crear usuario
        User user =new User();
        user.setName(name);
        user.setEmail(email);

        //Encriptar Contrasenya
        user.setPassword(passwordEncoder.encode(rawPassword));

        //Asignar rol por defecto
        user.setRole(User.Role.EMPLOYEE);

        //guardar en BD
        return userRepository.save(user);
    }

    // Crear usuario desde admin
    //
    // - Verifica si el email ya existe
    // - Encripta la contraseña
    // - Asigna rol EMPLOYEE por defecto
    // - Guarda el usuario
    public User createUser(User user) {

        // comprobar si ya existe email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        // encriptar password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // asignar rol EMPLOYEE
        user.setRole(User.Role.EMPLOYEE);

        logger.info("Admin ha creado al nuevo usuario "+ user.getName() + " con email " +user.getEmail());
        return userRepository.save(user);
    }


    // Elimina un usuario por su ID
    //
    // - Busca el usuario
    // - Si no existe → excepción
    // - Lo elimina de la base de datos
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        userRepository.delete(user);
        logger.info("Admin ha eliminado a usuario " + user.getName() + " con email " + user.getEmail());
    }

    // Devuelve la lista completa de usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
