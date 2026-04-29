package com.roomyapp.service;

import com.roomyapp.dto.ReservationRequest;
import com.roomyapp.entity.Reservation;
import com.roomyapp.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // 1. Crear reserva
    public Reservation createReservation(ReservationRequest request) {

        // 🔥 PARSEO (CLAVE)
        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime startTime = LocalTime.parse(request.getStartTime());
        LocalTime endTime = LocalTime.parse(request.getEndTime());

        // Validar solapamientos
        List<Reservation> existingReservations =
                reservationRepository.findByRoomIdAndDate(request.getRoomId(), date);

        for (Reservation existing : existingReservations) {
            if (isOverlapping(
                    existing.getStartTime(),
                    existing.getEndTime(),
                    startTime,
                    endTime
            )) {
                throw new RuntimeException("La sala ya está reservada en ese horario");
            }
        }

        // Crear reserva
        Reservation reservation = new Reservation();
        reservation.setRoomId(request.getRoomId());
        reservation.setUserId(request.getUserId());

        reservation.setDate(date);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setGuests(request.getGuests());

        return reservationRepository.save(reservation);
    }

    // 2. Obtener todas (admin)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // 3. Obtener por ID
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    // 4. Obtener reservas de un usuario
    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    // 5. Actualizar reserva
    public Reservation updateReservation(Long id, ReservationRequest request) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // 🔥 PARSEO
        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime startTime = LocalTime.parse(request.getStartTime());
        LocalTime endTime = LocalTime.parse(request.getEndTime());

        // Validar solapamientos (excluyendo la propia reserva)
        List<Reservation> existingReservations =
                reservationRepository.findByRoomIdAndDate(request.getRoomId(), date);

        for (Reservation existing : existingReservations) {
            if (!existing.getId().equals(id) &&
                    isOverlapping(
                            existing.getStartTime(),
                            existing.getEndTime(),
                            startTime,
                            endTime
                    )) {
                throw new RuntimeException("La sala ya está reservada en ese horario");
            }
        }

        // Actualizar
        reservation.setRoomId(request.getRoomId());
        reservation.setUserId(request.getUserId());
        reservation.setDate(date);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setGuests(request.getGuests());

        return reservationRepository.save(reservation);
    }

    // 6. Eliminar reserva
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reservationRepository.delete(reservation);
    }

    // 🔥 SOLUCIÓN DEFINITIVA DEL ERROR
    private boolean isOverlapping(
            LocalTime start1,
            LocalTime end1,
            LocalTime start2,
            LocalTime end2
    ) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}