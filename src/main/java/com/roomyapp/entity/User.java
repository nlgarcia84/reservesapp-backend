package com.roomyapp.entity;

import jakarta.persistence.*;

/*
 * Entidad que representa un usuario dentro del sistema.
 *
 * RESPONSABILIDAD:
 * - Mapear la tabla "users" de la base de datos
 * - Almacenar la información básica del usuario
 * - Definir el rol del usuario dentro de la aplicación
 *
 * ANOTACIONES JPA:
 * - @Entity → indica que es una entidad persistente
 * - @Table(name="users") → especifica el nombre de la tabla en la BD
 *
 * CAMPOS:
 * - id → identificador único (clave primaria, autogenerado)
 * - name → nombre del usuario (obligatorio)
 * - email → identificador único del usuario (login)
 * - password → contraseña encriptada (BCrypt)
 * - role → rol del usuario (ADMIN o EMPLOYEE)
 *
 * SEGURIDAD:
 * - La contraseña nunca se guarda en texto plano
 * - Siempre debe almacenarse encriptada
 *
 * IMPORTANTE:
 * - Esta clase NO contiene lógica de negocio
 * - Solo representa los datos (modelo de dominio)
 */

@Entity
@Table(name="users")
public class User {

    // Identificador único del usuario (clave primaria)
    // Se genera automáticamente en la base de datos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del usuario (campo obligatorio)
    @Column(nullable = false)
    private String name;

    // Email del usuario (debe ser único)
    // Se utiliza como identificador para login
    @Column(unique = true)
    private String email;

    // Contraseña del usuario
    // IMPORTANTE: se almacena encriptada (BCrypt), nunca en texto plano
    private String password;

    // Rol del usuario dentro del sistema
    // Se guarda como texto en la base de datos (STRING)
    @Enumerated(EnumType.STRING)
    private Role role;

    // Constructor vacío requerido por JPA
    public User(){}

    // Enumeración de roles disponibles en la aplicación
    // ADMIN → acceso completo
    // EMPLOYEE → acceso limitado
    public enum Role{
        ADMIN,
        EMPLOYEE
    }

    // Métodos getter y setter para acceder y modificar los campos
    // Necesarios para JPA y para el funcionamiento del framework
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
