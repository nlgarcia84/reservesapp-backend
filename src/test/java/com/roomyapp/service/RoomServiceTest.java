package com.roomyapp.service;

import com.roomyapp.entity.Room;
import com.roomyapp.repository.RoomRepository;
import com.roomyapp.dto.RoomRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    public RoomServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    //Test Positivo
    @Test
    void shouldUpdateRoomSuccessfully() {
        // GIVEN
        Room existingRoom = new Room();
        existingRoom.setId(1L);
        existingRoom.setName("Sala vieja");

        RoomRequest request = new RoomRequest();
        request.setName("Sala nueva");
        request.setCapacity(20);
        request.setDescription("Nueva desc");
        request.setImageUrl("url");

        request.setHasProjector(true);
        request.setHasWhiteboard(true);
        request.setHasTv(false);
        request.setHasAirConditioning(true);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(any(Room.class))).thenAnswer(i -> i.getArguments()[0]);

        // WHEN
        Room result = roomService.updateRoom(1L, request);

        // THEN
        assertNotNull(result);
        assertEquals("Sala nueva", result.getName());
        assertEquals(20, result.getCapacity());
        assertTrue(result.isHasProjector());
        assertTrue(result.isHasWhiteboard());
        assertFalse(result.isHasTv());
        assertTrue(result.isHasAirConditioning());
        verify(roomRepository).save(existingRoom);
    }

    @Test
    void shouldReturnAllRooms() {
        // GIVEN
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room());
        rooms.add(new Room());

        when(roomRepository.findAll()).thenReturn(rooms);

        // WHEN
        List<Room> result = roomService.getRooms();

        // THEN
        assertEquals(2, result.size());
        verify(roomRepository).findAll();
    }

    @Test
    void shouldDeleteRoomSuccessfully() {
        // GIVEN
        Room room = new Room();
        room.setId(1L);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        // WHEN
        roomService.deleteRoom(1L);

        // THEN
        verify(roomRepository).delete(room);
    }


    //Test Negativo
    @Test
    void shouldThrowExceptionIfRoomNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        RoomRequest request = new RoomRequest();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            roomService.updateRoom(1L, request);
        });

        assertTrue(exception.getMessage().contains("Sala no encontrada"));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingRoom() {
        // GIVEN
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN + THEN
        Exception exception = assertThrows(RuntimeException.class, () -> {
            roomService.deleteRoom(1L);
        });

        assertTrue(exception.getMessage().contains("Sala no encontrada"));
    }

}