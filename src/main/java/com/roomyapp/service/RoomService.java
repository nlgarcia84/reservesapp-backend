package com.roomyapp.service;

import com.roomyapp.dto.RoomRequest;
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
     * @param request datos de la sala recibidos desde el controlador (DTO)
     * @return sala guardada con ID generado
     */
    public Room createRoom(RoomRequest request){
        logger.info("RoomService.createRoom() - Guardando sala: " + request.getName());
        try {
            //Mapeamos DTO -> Entity
            Room room = new Room();

            room.setName(request.getName());
            room.setCapacity(request.getCapacity());
            room.setDescription(request.getDescription());
            room.setImageUrl(request.getImageUrl());
            room.setHasProjector(request.isHasProjector());
            room.setHasWhiteboard(request.isHasWhiteboard());
            room.setHasTv(request.isHasTv());
            room.setHasAirConditioning(request.isHasAirConditioning());

            Room savedRoom = roomRepository.save(room);
            logger.info("RoomService.createRoom() - Sala guardada con ID: " + savedRoom.getId());
            return savedRoom;
        } catch (Exception e) {
            logger.error("RoomService.createRoom() - Error guardando sala: " + e.getMessage(), e);
            throw new RuntimeException("Error al crear sala: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza y guarda una sala en la base de datos.
     *
     * @param request datos de la sala recibidos desde el controlador (DTO) y el ID de la sala a modificar
     * @return sala actulizada con ID enviado
     */

    public Room updateRoom(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        // actualizar campos
        room.setName(request.getName());
        room.setCapacity(request.getCapacity());
        room.setDescription(request.getDescription());
        room.setImageUrl(request.getImageUrl());

        room.setHasProjector(request.isHasProjector());
        room.setHasWhiteboard(request.isHasWhiteboard());
        room.setHasTv(request.isHasTv());
        room.setHasAirConditioning(request.isHasAirConditioning());

        return roomRepository.save(room);
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

    /**
     * Obtiene una sala por su ID.
     *
     * Busca en la base de datos una sala concreta.
     * Si no existe, lanza una excepción.
     *
     * @param id identificador de la sala
     * @return sala encontrada
     */
    public Room getRoomById(Long id) {
        logger.info("RoomService.getRoomById() - Buscando sala con ID: " + id);

        try {
            Room room = roomRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

            logger.info("RoomService.getRoomById() - Sala encontrada: " + room.getName());
            return room;

        } catch (Exception e) {
            logger.error("RoomService.getRoomById() - Error: " + e.getMessage(), e);
            throw new RuntimeException("Error al obtener sala: " + e.getMessage(), e);
        }
    }
}
