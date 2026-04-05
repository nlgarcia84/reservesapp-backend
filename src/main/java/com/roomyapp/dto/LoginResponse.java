package com.roomyapp.dto;

/*
 * DTO (Data Transfer Object) de respuesta para la autenticación (login/register).
 *
 * RESPONSABILIDAD:
 * - Enviar al frontend la información del usuario autenticado
 * - Incluir el token JWT necesario para futuras peticiones
 *
 * FLUJO:
 * - El backend genera este objeto tras login o registro exitoso
 * - Spring (Jackson) lo convierte automáticamente a JSON
 * - El frontend recibe este JSON y guarda el token
 *
 * DATOS INCLUIDOS:
 * - id: identificador del usuario
 * - name: nombre del usuario
 * - email: email del usuario
 * - role: rol (ADMIN o EMPLOYEE)
 * - token: JWT para autenticación en siguientes peticiones
 *
 * SEGURIDAD:
 * - NO se incluye la contraseña (dato sensible)
 *
 * IMPORTANTE:
 * - Es un DTO de salida (response)
 * - No contiene lógica de negocio
 * - Jackson utiliza los getters para construir el JSON de respuesta
 */
public class LoginResponse {

    // Identificador único del usuario
    private Long id;

    // Nombre del usuario
    private String name;

    // Email del usuario (identificador de login)
    private String email;

    // Rol del usuario (ADMIN o EMPLOYEE)
    private String role;

    // Token JWT generado tras autenticación
    private String token;

    // Constructor para crear la respuesta completa
    public LoginResponse(Long id, String name, String email, String role, String token){
        this.id=id;
        this.name=name;
        this.email=email;
        this.role=role;
        this.token=token;
    }

    // Getters → necesarios para que Jackson convierta el objeto a JSON
    public Long getId(){
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

    public String getToken(){
        return token;
    }
}
