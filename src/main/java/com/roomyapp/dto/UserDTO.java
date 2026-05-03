package com.roomyapp.dto;

/*
 * DTO que será utilizado por las reserevas de salas para enviar al front,
 * nombre e id del usuario creador de una reserva
 */
public class UserDTO {

    private Long id;
    private String name;

    //Constructores
    public UserDTO(){}

    public UserDTO(String name, Long id) {
        this.name = name;
        this.id = id;
    }


    //Getters y Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
