package com.reservas.reservas.repository;

import com.reservas.reservas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/*
  Interfaz Repositorio de entidad User
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
