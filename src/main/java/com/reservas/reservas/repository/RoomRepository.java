package com.reservas.reservas.repository;

import com.reservas.reservas.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

/*
  Interfaz repositorio de Entidad Room
 */
public interface RoomRepository extends JpaRepository<Room, Long> {

}
