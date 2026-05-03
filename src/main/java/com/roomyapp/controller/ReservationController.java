package com.roomyapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.roomyapp.dto.ReservationRequest;
import com.roomyapp.dto.ReservationResponse;
import com.roomyapp.service.ReservationService;

import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión de reservas.
 *
 * Expone endpoints para crear, consultar, actualizar y eliminar reservas.
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Obtener reservas de una sala (modo empleado)
     * Ya devuelve DTO desde el service → NO hacer map aquí
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getReservationsByRoom(@PathVariable Long roomId) {
        try {
            System.out.println("DEBUG: Buscant reserves per a la sala ID: " + roomId);

            List<ReservationResponse> reservations =
                    reservationService.getReservationsByRoom(roomId);

            System.out.println("DEBUG: Reserves trobades: " + reservations.size());

            return ResponseEntity.ok(reservations);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error en el servidor: " + e.getMessage());
        }
    }

    /**
     * NUEVO: reservas del usuario autenticado
     * Ya devuelve DTO desde el service
     */
    @GetMapping("/my-bookings")
    public List<ReservationResponse> getMyReservations(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal(); // ✔ ID

        return reservationService.getReservationsByUser(userId);
    }

    /**
     * Crear reserva
     * Creamos entity → la convertimos a DTO para devolver al front
     */
    @PostMapping
    public ReservationResponse createReservation(@Valid @RequestBody ReservationRequest request) {
        return
                reservationService.createReservation(request);
    }

    /**
     * Obtener todas las reservas (modo admin)
     * Ya devuelve DTO
     */
    @GetMapping
    public List<ReservationResponse> getAllReservations() {
        return reservationService.getAllReservations();
    }

    /**
     * Obtener reserva por ID
     */
    @GetMapping("/{id}")
    public ReservationResponse getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    /**
     * Obtener reservas de un usuario (modo empleado)
     */
    @GetMapping("/user/{userId}")
    public List<ReservationResponse> getReservationsByUser(@PathVariable Long userId) {
        return reservationService.getReservationsByUser(userId);
    }

    /**
     * Actualizar reserva
     */
    @PutMapping("/{id}")
    public ReservationResponse updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequest request
    ) {
        return reservationService.updateReservation(id, request);
    }

    /**
     * Eliminar reserva
     */
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }
}