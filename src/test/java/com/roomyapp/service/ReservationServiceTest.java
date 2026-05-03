package com.roomyapp.service;

import com.roomyapp.dto.ReservationRequest;
import com.roomyapp.dto.ReservationResponse;
import com.roomyapp.entity.Reservation;
import com.roomyapp.entity.Room;
import com.roomyapp.repository.ReservationRepository;
import com.roomyapp.repository.RoomRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    @Test
    void testSolapamiento_true() {

        ReservationService service = new ReservationService(null, null);

        LocalTime start1 = LocalTime.of(10, 0);
        LocalTime end1 = LocalTime.of(11, 0);

        LocalTime start2 = LocalTime.of(10, 30);
        LocalTime end2 = LocalTime.of(11, 30);

        boolean resultado = service.isOverlapping(start1, end1, start2, end2);

        assertTrue(resultado);
    }

    @Test
    void testSolapamiento_false() {

        ReservationService service = new ReservationService(null, null);

        LocalTime start1 = LocalTime.of(10, 0);
        LocalTime end1 = LocalTime.of(11, 0);

        LocalTime start2 = LocalTime.of(11, 0);
        LocalTime end2 = LocalTime.of(12, 0);

        boolean resultado = service.isOverlapping(start1, end1, start2, end2);

        assertFalse(resultado);
    }

    @Test
    void testCreateReservation_sinSolapamiento() {

        ReservationRepository repo = mock(ReservationRepository.class);
        RoomRepository roomRepo = mock(RoomRepository.class);

        ReservationService service = new ReservationService(repo, roomRepo);

        ReservationRequest request = new ReservationRequest();
        request.setRoomId(1L);
        request.setUserId(1L);
        request.setDate("2026-05-05");
        request.setStartTime("10:00");
        request.setEndTime("11:00");
        request.setGuests(List.of(2L, 3L));

        Reservation savedReservation = new Reservation();
        savedReservation.setRoomId(1L);
        savedReservation.setUserId(1L);
        savedReservation.setDate(java.time.LocalDate.of(2026, 5, 5));
        savedReservation.setStartTime(LocalTime.of(10, 0));
        savedReservation.setEndTime(LocalTime.of(11, 0));
        savedReservation.setGuests(List.of(2L, 3L));

        Room room = new Room();
        room.setId(1L);
        room.setName("Sala Reunions");

        when(repo.findByRoomIdAndDate(anyLong(), any()))
                .thenReturn(List.of());

        when(repo.save(any(Reservation.class)))
                .thenReturn(savedReservation);

        when(roomRepo.findById(1L))
                .thenReturn(Optional.of(room));

        ReservationResponse resultado = service.createReservation(request);

        assertNotNull(resultado);
        assertEquals("Sala Reunions", resultado.getRoomName());
        assertEquals(1L, resultado.getRoomId());
        assertEquals(1L, resultado.getUserId());

        verify(repo).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_conSolapamiento() {

        ReservationRepository repo = mock(ReservationRepository.class);
        RoomRepository roomRepo = mock(RoomRepository.class);

        ReservationService service = new ReservationService(repo, roomRepo);

        ReservationRequest request = new ReservationRequest();
        request.setRoomId(1L);
        request.setUserId(1L);
        request.setDate("2026-05-05");
        request.setStartTime("10:30");
        request.setEndTime("11:30");
        request.setGuests(List.of(2L, 3L));

        Reservation existente = new Reservation();
        existente.setStartTime(LocalTime.of(10, 0));
        existente.setEndTime(LocalTime.of(11, 0));

        when(repo.findByRoomIdAndDate(anyLong(), any()))
                .thenReturn(List.of(existente));

        assertThrows(RuntimeException.class, () -> {
            service.createReservation(request);
        });

        verify(repo, never()).save(any(Reservation.class));
    }
}
