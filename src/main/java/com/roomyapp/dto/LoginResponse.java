package com.roomyapp.dto;
/*
Clase que representa un DTO de salida
Decide enviar al frontend los datos del usuario, pero exceptuando la password, esto es información sensible.
Solo transporta los datos ente las capas.  Se envía token y rol
Jackson necesita getters para convertir el objet a JSON, si no hay getters no aparece en JSON
 */
public class LoginResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String token;

    public LoginResponse(Long id, String name, String email, String role, String token){
        this.id=id;
        this.name=name;
        this.email=email;
        this.role=role;
        this.token=token;
    }

    public Long getid(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getRole(){
        return role;
    }

    public String getToken(){return token;}
}
