package com.roomyapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomyapp.config.JwtUtil;
import com.roomyapp.dto.LoginRequest;
import com.roomyapp.entity.User;
import com.roomyapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * Tests del controlador de autenticación (AuthController)
 *
 * OBJETIVO:
 * - Verificar el funcionamiento de los endpoints REST (/auth/login y /auth/register)
 * - Validar la correcta respuesta JSON del backend
 * - Comprobar la integración con UserService y JwtUtil (mockeados)
 *
 * IMPORTANTE:
 * - Se utiliza @WebMvcTest para cargar solo la capa web (controller)
 * - Se mockean las dependencias (UserService y JwtUtil)
 * - Se desactiva la seguridad (@AutoConfigureMockMvc(addFilters = false))
 *   para centrarse únicamente en la lógica del controller
 *
 * NOTA:
 * - No se testea aquí la seguridad (JWT o filtros), eso se haría en tests específicos
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    // TEST 1: Login correcto
    @Test
    void shouldLoginSuccessfully() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@mail.com");
        request.setPassword("1234");

        User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("test@mail.com");
        user.setRole(User.Role.EMPLOYEE);

        when(userService.login("test@mail.com", "1234"))
                .thenReturn(user);

        when(jwtUtil.generateToken("test@mail.com", "EMPLOYEE", false, 1L))
                .thenReturn("fake-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    // TEST 2: Login error
    @Test
    void shouldFailLogin() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@mail.com");
        request.setPassword("1234");

        when(userService.login("wrong@mail.com", "1234"))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // TEST 3: Register correcto
    @Test
    void shouldRegisterSuccessfully() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("test@mail.com");
        user.setRole(User.Role.EMPLOYEE);

        when(userService.register("Test", "test@mail.com", "1234"))
                .thenReturn(user);

        when(jwtUtil.generateToken("test@mail.com", "EMPLOYEE", false, 1L))
                .thenReturn("fake-jwt");

        String json = """
        {
            "name":"Test",
            "email":"test@mail.com",
            "password":"1234"
        }
        """;

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.token").value("fake-jwt"));
    }
}