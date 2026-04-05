package com.roomyapp.dto;

/*
 * DTO (Data Transfer Object) utilizado para recibir los datos de login
 * enviados desde el frontend.
 *
 * RESPONSABILIDAD:
 * - Transportar los datos necesarios para la autenticación del usuario
 *   (email, contraseña y opción rememberMe)
 *
 * FLUJO:
 * - El frontend envía un JSON con los datos de login
 * - Spring (Jackson) convierte automáticamente ese JSON en este objeto
 * - El controlador utiliza este objeto para autenticar al usuario
 *
 * rememberMe:
 * - true  → el backend generará un token con mayor duración (más largo)
 * - false → el token tendrá una expiración estándar
 *
 * IMPORTANTE:
 * - Este objeto solo se usa para entrada de datos (request)
 * - No contiene lógica de negocio
 */
public class LoginRequest {

    // Email del usuario (identificador para login)
    private String email;

    // Contraseña en texto plano enviada desde el frontend
    private String password;

    // Frontend le dice Backend, recuerdame (rememberMe true).
    private boolean rememberMe;

    // Constructor vacío necesario para que Jackson pueda crear el objeto desde JSON
    public LoginRequest() {
    }

    // Constructor con argumentos
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters → permiten acceder a los valores desde el controlador
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public boolean getRememberMe(){
        return rememberMe;
    }

    // Setters → necesarios para que Jackson asigne valores desde el JSON
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRememberMe(boolean rememberMe){this.rememberMe=rememberMe;}

}
