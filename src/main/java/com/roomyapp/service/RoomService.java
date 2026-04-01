package com.roomyapp.service;

import com.roomyapp.entity.Room;
import com.roomyapp.entity.User;
import com.roomyapp.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);
    private final RoomRepository roomRepository;

    //Inyeccion de repositorio dentro del servicio
    public RoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
        logger.info("RoomService inicializado");
    }

    //Obtener todas las salas
    public List<Room> getRooms(){
        logger.info("RoomService.getRooms() - Consultando base de datos");
        try {
            List<Room> rooms = roomRepository.findAll();
            logger.info("RoomService.getRooms() - Se encontraron " + rooms.size() + " salas");
            return rooms;
        } catch (Exception e) {
            logger.error("RoomService.getRooms() - Error consultando base de datos: " + e.getMessage(), e);
            throw new RuntimeException("Error al obtener salas de la base de datos: " + e.getMessage(), e);
        }
    }

    //Crear una nueva sala
    public Room createRoom(Room room){
        logger.info("RoomService.createRoom() - Guardando sala: " + room.getName());
        try {
            Room savedRoom = roomRepository.save(room);
            logger.info("RoomService.createRoom() - Sala guardada con ID: " + savedRoom.getId());
            return savedRoom;
        } catch (Exception e) {
            logger.error("RoomService.createRoom() - Error guardando sala: " + e.getMessage(), e);
            throw new RuntimeException("Error al crear sala: " + e.getMessage(), e);
        }
    }

    // Eliminar usuario
    public void deleteRoom(Long id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        roomRepository.delete(room);
    }
}
