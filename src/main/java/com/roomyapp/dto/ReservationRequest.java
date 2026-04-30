package com.roomyapp.dto;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

/*
 * DTO para la creación y actualización de reservas.
 *
 * Representa los datos que llegan desde el frontend.
 */
public class ReservationRequest {

    @NotNull
    @JsonProperty("room_id")
    private Long room_id;


    @JsonProperty("user_id")
    private Long user_id;

    @NotBlank
    private String date;

    @NotBlank
    @JsonProperty("start_time")
    private String start_time;

    @NotBlank
    @JsonProperty("end_time")
    private String end_time;

    private List<Long> guests;

    // Constructor vacío
    public ReservationRequest() {}

    // Getters y Setters

    public Long getRoomId() {
        return room_id;
    }

    public void setRoomId(Long room_id) {
        this.room_id = room_id;
    }

    public Long getUserId() {
        return user_id;
    }

    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return start_time;
    }

    public void setStartTime(String start_time) {
        this.start_time = start_time;
    }

    public String getEndTime() {
        return end_time;
    }

    public void setEndTime(String end_time) {
        this.end_time = end_time;
    }

    public List<Long> getGuests() {
        return guests;
    }

    public void setGuests(List<Long> guests) {
        this.guests = guests;
    }

}
