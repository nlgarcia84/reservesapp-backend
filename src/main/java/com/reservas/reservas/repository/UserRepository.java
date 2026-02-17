package com.reservas.reservas.repository;

import com.reservas.reservas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
  Interfaz Repositorio de entidad User
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);

}
