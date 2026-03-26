package com.reservas.reservas.service;

import com.reservas.reservas.entity.User;
import com.reservas.reservas.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    //Constructor con inyección de dependencia de Repositorio
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public User login(String email, String password){
        return userRepository
                .findByEmailAndPassword(email, password)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }
}
