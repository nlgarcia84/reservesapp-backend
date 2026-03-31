package com.roomyapp.dto;

public class LoginRequest {
    private String email;
    private String password;

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
}
