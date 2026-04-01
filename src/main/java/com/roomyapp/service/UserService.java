package com.roomyapp.service;

import com.roomyapp.dto.RegisterRequest;
import com.roomyapp.entity.User;
import com.roomyapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //Constructor con inyección de dependencia de Repositorio
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //Login
    public User login(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }
        return user;
    }

    //Register
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
    public User createUser(User user) {

        // comprobar si ya existe email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        // encriptar password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // asignar rol EMPLOYEE
        user.setRole(User.Role.EMPLOYEE);

        return userRepository.save(user);
    }


    // Eliminar usuario
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        userRepository.delete(user);
    }

    // Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
