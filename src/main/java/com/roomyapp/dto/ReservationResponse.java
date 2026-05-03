package com.roomyapp.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationResponse {

    private Long id;
    private Long roomId;
    private String roomName;
    private Long userId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<Long> guests;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public List<Long> getGuests() { return guests; }
    public void setGuests(List<Long> guests) { this.guests = guests; }
}
