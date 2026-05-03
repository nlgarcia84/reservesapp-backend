package com.roomyapp.dto;

/*
 *DTO que envía al front capsula de informacion de la sala reservada con id y su nombre
 */
public class RoomDTO {

    private Long id;
    private String name;

    //Constructores
    public RoomDTO(){};

    public RoomDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    //Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
