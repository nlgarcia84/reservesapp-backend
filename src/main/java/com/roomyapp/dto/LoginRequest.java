package com.roomyapp.dto;

/*
Capa que recoge el email, password y booleano remeberMe desde el front, al momento
del login
En caso de rememberMe verdadero, el front devolvera un token mas largo
 */
public class LoginRequest {
    private String email;
    private String password;
    private boolean rememberMe; //Frontend le dice Backend, recuerdame (rememberMe true).

    // Constructor sin argumentos (necesario para Jackson)
    public LoginRequest() {
    }

    // Constructor con argumentos
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    // Setters (necesarios para Jackson)
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean getRememberMe(){
        return rememberMe;
    }
}
