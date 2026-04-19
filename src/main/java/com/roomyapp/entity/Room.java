package com.roomyapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

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
    private String description;
    private String imageUrl;
    private boolean hasProjector;
    private boolean hasWhiteboard;
    private boolean hasTv;
    private boolean hasAirConditioning;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isHasProjector() {
        return hasProjector;
    }

    public void setHasProjector(boolean hasProjector) {
        this.hasProjector = hasProjector;
    }

    public boolean isHasWhiteboard() {
        return hasWhiteboard;
    }

    public void setHasWhiteboard(boolean hasWhiteboard) {
        this.hasWhiteboard = hasWhiteboard;
    }

    public boolean isHasTv() {
        return hasTv;
    }

    public void setHasTv(boolean hasTv) {
        this.hasTv = hasTv;
    }

    public boolean isHasAirConditioning() {
        return hasAirConditioning;
    }

    public void setHasAirConditioning(boolean hasAirConditioning) {
        this.hasAirConditioning = hasAirConditioning;
    }

    @JsonProperty("equipment")
    public List<String> getEquipment(){
        List<String> equipment = new ArrayList<>();
         if(hasProjector) equipment.add("PROJECTOR");
         if(hasWhiteboard) equipment.add("WHITEBOARD");
         if(hasTv) equipment.add("TV");
         if(hasAirConditioning) equipment.add("AIR");

         return equipment;
    }
}
