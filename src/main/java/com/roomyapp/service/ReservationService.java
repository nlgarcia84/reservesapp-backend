package com.roomyapp.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.roomyapp.dto.ReservationResponse;
import com.roomyapp.dto.RoomDTO;
import com.roomyapp.dto.UserDTO;
import com.roomyapp.entity.Room;
import com.roomyapp.entity.User;
import com.roomyapp.repository.RoomRepository;
import com.roomyapp.repository.UserRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.roomyapp.dto.ReservationRequest;
import com.roomyapp.entity.Reservation;
import com.roomyapp.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    //Inyección de repositorios necesarios para dar informacion
    //completa de la reserva
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    //Constructor
    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    // 1. Crear reserva
    public ReservationResponse createReservation(ReservationRequest request) {

        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime startTime = LocalTime.parse(request.getStartTime());
        LocalTime endTime = LocalTime.parse(request.getEndTime());

        // Validar solapamientos
        List<Reservation> existingReservations
                = reservationRepository.findByRoomIdAndDate(request.getRoomId(), date);

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

        // Guardar en BD
        Reservation saved = reservationRepository.save(reservation);

        // devolver DTO (IMPORTANTE)
        return mapToResponse(saved);
    }

    // 2. Obtener todas (admin)
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 3. Obtener por ID
    public ReservationResponse getReservationById(@NonNull Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        return mapToResponse(reservation);
    }

    // 4. Obtener reservas de un usuario
    public List<ReservationResponse> getReservationsByUser(Long userId) {
        return reservationRepository.findAllByUserOrGuest(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 5. Actualizar reserva
    public ReservationResponse updateReservation(@NonNull Long id, ReservationRequest request) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // PARSEO
        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime startTime = LocalTime.parse(request.getStartTime());
        LocalTime endTime = LocalTime.parse(request.getEndTime());

        // Validar solapamientos (excluyendo la propia reserva)
        List<Reservation> existingReservations
                = reservationRepository.findByRoomIdAndDate(request.getRoomId(), date);

        for (Reservation existing : existingReservations) {
            if (!existing.getId().equals(id)
                    && isOverlapping(
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

        // Guardar
        Reservation updated = reservationRepository.save(reservation);

        //  IMPORTANTE devolver DTO
        return mapToResponse(updated);
    }

    // 6. Eliminar reserva
    public void deleteReservation(@NonNull Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reservationRepository.delete(reservation);
    }

    // SOLUCIÓN DEFINITIVA DEL ERROR
    public boolean isOverlapping(
            LocalTime start1,
            LocalTime end1,
            LocalTime start2,
            LocalTime end2
    ) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    // 7. Obtener reservas de una sala (modo empleado)
    public List<ReservationResponse> getReservationsByRoom(Long roomId) {
        return reservationRepository.findByRoomId(roomId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 8. Método mapper para convertir de nuevo la Entity en DTO para ser enviada al front como respuesta
    public ReservationResponse mapToResponse(Reservation reservation) {
        ReservationResponse dto = new ReservationResponse();

        //básicos
        dto.setId(reservation.getId());
        dto.setDate(reservation.getDate().toString());
        dto.setStartTime(reservation.getStartTime().toString());
        dto.setEndTime(reservation.getEndTime().toString());

        dto.setRoomId(reservation.getRoomId());
        dto.setUserId(reservation.getUserId());
        // ROOM
        Room room = roomRepository.findById(reservation.getRoomId())
                .orElse(null);

        if (room != null) {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setId(room.getId());
            roomDTO.setName(room.getName());
            dto.setRoom(roomDTO);
        }

        // ORGANIZER
        User user = userRepository.findById(reservation.getUserId())
                .orElse(null);

        if (user != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            dto.setOrganizer(userDTO);
        }

        // GUESTS
        if (reservation.getGuests() != null) {
            List<UserDTO> guestDTOs = reservation.getGuests().stream()
                    .map(userId -> {
                        User guest = userRepository.findById(userId).orElse(null);
                        if (guest == null) return null;

                        UserDTO guestDTO = new UserDTO();
                        guestDTO.setId(guest.getId());
                        guestDTO.setName(guest.getName());
                        return guestDTO;
                    })
                    .filter(g -> g != null)
                    .toList();

            dto.setGuests(guestDTOs);
        }

        return dto;
    }

}
