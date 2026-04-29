package com.roomyapp.controller;

import com.roomyapp.dto.ReservationRequest;
import com.roomyapp.entity.Reservation;
import com.roomyapp.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;

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

    // NUEVO: reservas del usuario autenticado

    @GetMapping("/my-bookings")
    public List<Reservation> getMyReservations(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal(); // ✔ ID
        String email = (String) authentication.getDetails(); // ✔ email

        return reservationService.getReservationsByUser(userId);
    }


   /*

    @GetMapping("/my-bookings")
    public List<Reservation> getMyReservations(Authentication authentication) {
        String email = authentication.getName(); // 👈 viene del token
        return reservationService.getReservationsByUser(email);
    }
    */


    // 1. Crear reserva
    @PostMapping
    public Reservation createReservation(@Valid @RequestBody ReservationRequest request) {
        return reservationService.createReservation(request);
    }

    // 2. Obtener todas (modo admin)
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    // 3. Obtener por ID
    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    // 4. Obtener reservas de un usuario (modo empleado)
    @GetMapping("/user/{userId}")
    public List<Reservation> getReservationsByUser(@PathVariable Long userId) {
        return reservationService.getReservationsByUser(userId);
    }

    // 5. Actualizar reserva
    @PutMapping("/{id}")
    public Reservation updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequest request
    ) {
        return reservationService.updateReservation(id, request);
    }

    // 6. Eliminar reserva
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }
}