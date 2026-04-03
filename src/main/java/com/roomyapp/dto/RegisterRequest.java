package com.roomyapp.dto;

public class RegisterRequest {

    private String name;
    private String email;
    private String password;

    // Constructor vacío (IMPORTANTE para Jackson)
    public RegisterRequest() {}

    // Constructor con argumentos
    public RegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + (password != null ? "*****" : "null") + '\'' +
                '}';
    }
}

