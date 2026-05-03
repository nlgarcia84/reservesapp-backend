package com.roomyapp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.roomyapp.entity.Reservation;

/**
 * Repositorio de acceso a datos para la entidad Reservation.
 *
 * Permite realizar operaciones CRUD automáticamente gracias a JpaRepository sin
 * escribir SQL
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Buscar reservas por sala y fecha (lo usaremos para validar solapamientos)
    List<Reservation> findByRoomIdAndDate(Long roomId, LocalDate date);

    // Buscar reservas de un usuario (modo empleado)
    List<Reservation> findByUserId(Long userId);

    /**
     * Busca reservas donde el usuario es el creador (userId) O su ID se encuentre
     * dentro de la lista de invitados (guests).
     */
    @Query("SELECT DISTINCT r FROM Reservation r "
            + "LEFT JOIN r.guests g "
            + "WHERE r.userId = :userId OR g = :userId")
    List<Reservation> findAllByUserOrGuest(@Param("userId") Long userId);

    // Buscar todas las reservas de una sala ordenadas por fecha y hora de inicio
    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId ORDER BY r.date ASC, r.startTime ASC")
    List<Reservation> findByRoomId(@Param("roomId") Long roomId);

}
