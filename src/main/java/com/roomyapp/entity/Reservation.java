package com.roomyapp.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    @JsonFormat(pattern = "HH:mm") 
    private LocalTime startTime;

    @Column(name = "end_time")
    @JsonFormat(pattern = "HH:mm") 
    private LocalTime endTime;

    //para evitar bugs y controlar la tabla de invitados
    @ElementCollection
    @CollectionTable(name = "reservation_guests", joinColumns = @JoinColumn(name = "reservation_id"))
    @Column(name = "guest")
    private List<Long> guests;

    public Reservation() {
    }

    // getters & setters
    public Long getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public List<Long> getGuests() {
        return guests;
    }

    public void setGuests(List<Long> guests) {
        this.guests = guests;
    }
}
