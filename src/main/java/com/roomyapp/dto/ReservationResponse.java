package com.roomyapp.dto;

import java.util.List;

/*
 * DTO que controla los datos que se enviarán al front
 * cuando se cree o se actualice una reserva
 * Salida y JPA --> las variables han der ser camelCase
 */
public class ReservationResponse {

    private Long id;
    private String date;
    private String startTime;
    private String endTime;

    private Long userId;
    private Long roomId;

    private RoomDTO room;//información de la sala
    private List<UserDTO> guests; //información de los invitados a la reunión
    private UserDTO organizer;       // información del creador de la reserva

    //Constructores

    public ReservationResponse() {};

    public ReservationResponse(Long id, String date, String startTime, String endTime, Long userId, Long roomId, RoomDTO room, List<UserDTO> guests, UserDTO organizer) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.roomId = roomId;
        this.room = room;
        this.guests = guests;
        this.organizer = organizer;
    }


    //Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }

    public List<UserDTO> getGuests() {
        return guests;
    }

    public void setGuests(List<UserDTO> guests) {
        this.guests = guests;
    }

    public UserDTO getOrganizer() {
        return organizer;
    }

    public void setOrganizer(UserDTO organizer) {
        this.organizer = organizer;
    }
}
