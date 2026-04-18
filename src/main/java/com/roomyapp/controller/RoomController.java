package com.roomyapp.controller;

import com.roomyapp.entity.Room;
import com.roomyapp.service.RoomService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controlador REST para la gestión de salas.
 *
 * Expone endpoints relacionados con las salas:
 * - Obtener listado de salas
 * - Crear nuevas salas
 * - Eliminar salas
 *
 * Este controlador actúa como intermediario entre el frontend y la capa de servicio.
 * Recibe las peticiones HTTP y delega la lógica en RoomService.
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
    private RoomService roomService;

    /**
     * Constructor con inyección de dependencia del servicio de salas.
     *
     * @param roomService servicio que contiene la lógica de negocio de salas
     */
    public RoomController (RoomService roomService){
        this.roomService = roomService;
    }

    /**
     * Endpoint para obtener todas las salas.
     *
     * Metodo: GET /rooms
     *
     * @return lista de salas disponibles
     */
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

    /**
     * Endpoint para obtener una sala por ID.
     *
     * Método: GET /rooms/{id}
     *
     * @param id identificador de la sala
     * @return sala encontrada
     */
    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) {

        logger.info("GET /rooms/{} - Obteniendo detalle de sala", id);

        try {
            return roomService.getRoomById(id);

        } catch (Exception e) {
            logger.error("Error al obtener sala: " + e.getMessage(), e);
            throw e;
        }
    }


    /**
     * Endpoint para crear una nueva sala.
     *
     * Metodo: POST /rooms
     *
     * @param room objeto Room recibido desde el frontend
     * @return sala creada y persistida en base de datos
     */
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
    /**
     * Endpoint para eliminar una sala por ID.
     *
     * Metodo: DELETE /rooms/{id}
     *
     * @param id identificador de la sala a eliminar
     */
    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }

}
