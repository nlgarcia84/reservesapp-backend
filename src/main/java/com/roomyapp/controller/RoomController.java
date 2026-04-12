package com.roomyapp.controller;

import com.roomyapp.entity.Room;
import com.roomyapp.service.RoomService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://reservesapp-frontend-5clg.vercel.app"
})
public class RoomController {
    
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
    private RoomService roomService;

    //Constructor con inyeccion del servicio como parámetro
    public RoomController (RoomService roomService){
        this.roomService = roomService;
    }

    //Obtener todas las salas
    @GetMapping
    public List<Room> getRooms(){
        logger.info("GET /rooms - Obteniendo lista de salas");
        try {
            List<Room> rooms = roomService.getRooms();
            logger.info("Se encontraron " + rooms.size() + " salas");
            return rooms;
        } catch (Exception e) {
            logger.error("Error al obtener salas: " + e.getMessage(), e);
            throw e;
        }
    }

    //Crear una nueva sala
    @PostMapping
    public Room createRooms(@RequestBody Room room){
        logger.info("POST /rooms - Creando nueva sala: " + room.getName() + " con capacidad: " + room.getCapacity());
        try {
            Room createdRoom = roomService.createRoom(room);
            logger.info("Sala creada exitosamente con ID: " + createdRoom.getId());
            return createdRoom;
        } catch (Exception e) {
            logger.error("Error al crear sala: " + e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }

}
