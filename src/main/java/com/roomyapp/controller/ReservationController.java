package com.roomyapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roomyapp.dto.ReservationRequest;
import com.roomyapp.entity.Reservation;
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

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getReservationsByRoom(@PathVariable Long roomId) {
        try {
            System.out.println("DEBUG: Buscant reserves per a la sala ID: " + roomId);
            List<Reservation> reservations = reservationService.getReservationsByRoom(roomId);
            System.out.println("DEBUG: Reserves trobades: " + reservations.size());
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            // AIXÒ IMPRIMIRÀ L'ERROR REAL A LA CONSOLA DE L'IDE
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error en el servidor: " + e.getMessage());
        }
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
