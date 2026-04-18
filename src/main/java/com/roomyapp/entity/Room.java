package com.roomyapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entidad que representa una sala dentro del sistema.
 *
 * Esta clase está mapeada a una tabla en la base de datos mediante JPA.
 *
 * Contiene la información básica de una sala:
 * - Identificador único
 * - Nombre de la sala
 * - Capacidad máxima de personas
 *
 * Es utilizada en la gestión de salas y en futuras funcionalidades
 * como reservas.
 */
@Entity
public class Room {
    /**
     * Identificador único de la sala.
     * Se genera automáticamente en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int capacity;

    //Constructor
    public Room(){}

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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
