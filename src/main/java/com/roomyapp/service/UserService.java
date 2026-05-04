package com.roomyapp.service;

import com.roomyapp.dto.RegisterRequest;
import com.roomyapp.entity.User;
import com.roomyapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Servei que gestiona la lògica de negoci dels usuaris
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Login: valida email i contrasenya
    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }
        return user;
    }

    // Registre de nou usuari amb rol EMPLOYEE per defecte
    public User register(String name, String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("El usuari ja existeix");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(User.Role.EMPLOYEE);

        return userRepository.save(user);
    }

    // Crear usuari des del panell d'admin
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El usuari ja existeix");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.EMPLOYEE);

        logger.info("Admin ha creado al nuevo usuario " + user.getName() + " con email " + user.getEmail());
        return userRepository.save(user);
    }

    // Esborrar usuari per ID
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        userRepository.delete(user);
        logger.info("Admin ha eliminado a usuario " + user.getName() + " con email " + user.getEmail());
    }

    // Obtenir tots els usuaris
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Mapa d'usuaris actius: userId -> timestamp
    // Els usuaris s'expiren després de 5 minuts sense activitat
    private final Map<Long, Long> activeUsers = new ConcurrentHashMap<>();

    // Marcar usuari actiu
    public void markUserAsActive(Long userId) {
        activeUsers.put(userId, System.currentTimeMillis());
    }

    // Contar usuaris actius (elimina els expirats)
    public long getOnlineUsersCount() {
        long now = System.currentTimeMillis();
        activeUsers.entrySet().removeIf(entry -> now - entry.getValue() > 5 * 60 * 1000);
        return activeUsers.size();
    }

    // Obtenir mapa d'usuaris actius (debug)
    public Map<Long, Long> getActiveUsersMap() {
        return activeUsers;
    }

}
