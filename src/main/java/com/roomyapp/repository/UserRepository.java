package com.roomyapp.repository;

import com.roomyapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * Repositorio de acceso a datos para la entidad User.
 *
 * RESPONSABILIDAD:
 * - Gestionar la comunicación con la base de datos
 * - Proporcionar operaciones CRUD sobre la entidad User
 *
 * FUNCIONAMIENTO:
 * - Extiende JpaRepository, lo que proporciona automáticamente:
 *   - save() → guardar o actualizar usuario
 *   - findById() → buscar por ID
 *   - findAll() → obtener todos los usuarios
 *   - delete() → eliminar usuario
 *
 * MÉTODOS PERSONALIZADOS:
 * - findByEmail(String email):
 *   Permite buscar un usuario por su email
 *   Se utiliza principalmente en el login y validaciones de registro
 *
 * IMPORTANTE:
 * - Spring Data JPA genera automáticamente la implementación
 * - No es necesario escribir código SQL manual
 * - Optional se utiliza para evitar NullPointerException y manejar ausencia de datos
 */
//El Repository habla con la BD. El servicio devuelve el Usuario
public interface UserRepository extends JpaRepository<User, Long> {

    // Busca un usuario por su email
    // Devuelve Optional para gestionar el caso en que no exista el usuario
    Optional<User> findByEmail(String email);
}
