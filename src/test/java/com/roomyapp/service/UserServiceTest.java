package com.roomyapp.service;

import com.roomyapp.entity.User;
import com.roomyapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
 * Tests unitarios para UserService.
 *
 * OBJETIVO:
 * - Verificar la lógica de negocio sin acceder a la base de datos real
 * - Simular el comportamiento del repositorio usando Mockito
 *
 * IMPORTANTE:
 * - No se modifica el código original
 * - No se usa Supabase ni BD real
 */
class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    // TEST 1: Login correcto
    @Test
    void shouldLoginSuccessfully() {

        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encoded");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("1234", "encoded"))
                .thenReturn(true);

        User result = userService.login("test@mail.com", "1234");

        assertNotNull(result);
        assertEquals("test@mail.com", result.getEmail());
    }

    // TEST 2: Password incorrecta
    @Test
    void shouldFailLoginWrongPassword() {

        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encoded");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "encoded"))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () ->
                userService.login("test@mail.com", "wrong"));
    }

    // TEST 3: Register correcto
    @Test
    void shouldRegisterUser() {

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("1234"))
                .thenReturn("encoded");

        User savedUser = new User();
        savedUser.setEmail("test@mail.com");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        User result = userService.register("Test", "test@mail.com", "1234");

        assertNotNull(result);
        assertEquals("test@mail.com", result.getEmail());
    }

    // TEST 4: Usuario ya existe
    @Test
    void shouldFailRegisterIfUserExists() {

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () ->
                userService.register("Test", "test@mail.com", "1234"));
    }

    // TEST 5: Delete user
    @Test
    void shouldDeleteUser() {

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user);
    }
}
