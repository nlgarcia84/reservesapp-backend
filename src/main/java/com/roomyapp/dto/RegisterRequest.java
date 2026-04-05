package com.roomyapp.dto;

/*
 * DTO (Data Transfer Object) utilizado para recibir los datos de registro
 * de un nuevo usuario desde el frontend.
 *
 * RESPONSABILIDAD:
 * - Transportar los datos necesarios para crear un usuario
 *   (nombre, email y contraseña)
 *
 * FLUJO:
 * - El frontend envía un JSON con los datos del formulario de registro
 * - Spring (Jackson) convierte automáticamente ese JSON en este objeto
 * - El controlador utiliza este DTO para crear el usuario en el sistema
 *
 * SEGURIDAD:
 * - La contraseña se recibe en texto plano
 * - Será encriptada posteriormente en el servicio (UserService) usando BCrypt
 *
 * IMPORTANTE:
 * - Es un DTO de entrada (request)
 * - No contiene lógica de negocio
 * - Jackson requiere un constructor vacío y setters para mapear el JSON
 */
public class RegisterRequest {

    // Nombre del usuario
    private String name;

    // Email del usuario (será su identificador único)
    private String email;

    // Contraseña en texto plano (se encripta antes de guardar)
    private String password;

    // Constructor vacío necesario para Jackson
    public RegisterRequest() {}

    // Constructor con argumentos
    public RegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters → permiten acceder a los datos en el controlador
    // Setters → necesarios para que Jackson asigne valores desde el JSON
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

    // Metodo toString sobrescrito para depuración (logs)
    // IMPORTANTE: no muestra la contraseña real por seguridad
    @Override
    public String toString() {
        return "RegisterRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + (password != null ? "*****" : "null") + '\'' +
                '}';
    }
}

