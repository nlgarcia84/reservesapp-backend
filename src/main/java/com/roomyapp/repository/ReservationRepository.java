package com.roomyapp.repository;

import com.roomyapp.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad Reservation.
 *
 * Permite realizar operaciones CRUD automáticamente gracias a JpaRepository sin escribir SQL
 */
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    //Buscar reservas por sala y fecha (lo usaremos para validar solapamientos)
    List<Reservation> findByRoomIdAndDate(Long roomId, LocalDate date);

    //Buscar reservas de un usuario (modo empleado)
    List<Reservation> findByUserId(Long userId);
}
