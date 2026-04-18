package com.roomyapp.service;

import com.roomyapp.entity.Room;
import com.roomyapp.entity.User;
import com.roomyapp.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Servicio encargado de la lógica de negocio relacionada con las salas.
 *
 * Actúa como intermediario entre el controlador (RoomController)
 * y el repositorio (RoomRepository).
 *
 * Se encarga de:
 * - Obtener salas desde la base de datos
 * - Crear nuevas salas
 * - Eliminar salas existentes
 *
 * Aquí es donde se debería añadir cualquier validación o lógica adicional
 * antes de acceder a la base de datos.
 */
@Service
public class RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);
    private final RoomRepository roomRepository;

    /**
     * Constructor con inyección de dependencia del repositorio.
     *
     * @param roomRepository repositorio de acceso a datos de salas
     */
    public RoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
        logger.info("RoomService inicializado");
    }

    /**
     * Obtiene todas las salas de la base de datos.
     *
     * @return lista de salas
     */
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

    /**
     * Crea y guarda una nueva sala en la base de datos.
     *
     * @param room objeto Room recibido desde el controlador
     * @return sala guardada con ID generado
     */
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

    /**
     * Elimina una sala por su ID.
     *
     * Primero verifica que la sala existe.
     *
     * @param id identificador de la sala
     */
    public void deleteRoom(Long id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        roomRepository.delete(room);
    }
}
