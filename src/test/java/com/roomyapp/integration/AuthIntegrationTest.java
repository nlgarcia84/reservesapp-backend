package com.roomyapp.integration;

import com.roomyapp.dto.LoginRequest;
import com.roomyapp.dto.LoginResponse;
import com.roomyapp.entity.User;
import com.roomyapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/*
 * TEST DE INTEGRACIÓN COMPLETO
 *
 * OBJETIVO:
 * - Simular comportamiento real del frontend
 * - Verificar autenticación (login)
 * - Verificar autorización (roles)
 *
 * ESCENARIOS:
 * 1. EMPLOYEE puede ver salas (GET /rooms)
 * 2. EMPLOYEE NO puede crear salas (POST /rooms)
 * 3. ADMIN puede crear salas (POST /rooms)
 *
 * IMPORTANTE:
 * - Se levanta toda la aplicación real (@SpringBootTest)
 * - Se usa base de datos real (o configurada)
 * - Se usan tokens JWT reales
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TestRestTemplate restTemplate;

    // Helper para construir URL
    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    // Helper para hacer login y obtener token
    private String loginAndGetToken(String email, String password) {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        ResponseEntity<LoginResponse> response =
                restTemplate.postForEntity(
                        url("/auth/login"),
                        request,
                        LoginResponse.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        return response.getBody().getToken();
    }

    private void createEmployeeUser() {
        if (userRepository.findByEmail("employee1@test.com").isEmpty()) {
            User user = new User();
            user.setName("Employee Test");
            user.setEmail("employee1@test.com");
            user.setPassword(passwordEncoder.encode("123456A"));
            user.setRole(User.Role.EMPLOYEE);

            userRepository.save(user);
        }
    }

    // TEST 1: EMPLOYEE puede ver salas
    @Test
    void employeeShouldAccessRooms() {
        createEmployeeUser();

        String token = loginAndGetToken(
                "employee1@test.com",
                "123456A"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url("/rooms"),
                        HttpMethod.GET,
                        entity,
                        String.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    // TEST 2: EMPLOYEE NO puede crear sala
    @Test
    void employeeShouldNotCreateRoom() {

        createEmployeeUser();

        String token = loginAndGetToken(
                "employee1@test.com",
                "123456A"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = """
        {
            "name": "Sala Test",
            "capacity": 10
        }
        """;

        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url("/rooms"),
                        HttpMethod.POST,
                        entity,
                        String.class
                );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    private void createAdminUser() {
        if (userRepository.findByEmail("admin1@test.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin Test");
            admin.setEmail("admin1@test.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);

            userRepository.save(admin);
        }
    }


    // TEST 3: ADMIN puede crear sala
    @Test
    void adminShouldCreateRoom() {

        createAdminUser(); // IMPORTANTE

        String token = loginAndGetToken(
                "admin1@test.com",
                "admin123"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = """
    {
        "name": "Sala Admin",
        "capacity": 20
    }
    """;

        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url("/rooms"),
                        HttpMethod.POST,
                        entity,
                        String.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}