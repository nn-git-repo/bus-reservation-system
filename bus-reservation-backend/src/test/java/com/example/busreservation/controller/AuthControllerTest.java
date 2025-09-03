package com.example.busreservation.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.busreservation.entity.Role;
import com.example.busreservation.entity.User;
import com.example.busreservation.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testRegister_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        when(authService.register("Test User", "test@example.com", "pass123", "1234567890"))
            .thenReturn(user);

        AuthController.RegisterRequest req = new AuthController.RegisterRequest();
        req.setName("Test User");
        req.setEmail("test@example.com");
        req.setPassword("pass123");
        req.setPhone("1234567890");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Test User"))
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testLogin_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        Map<String, Object> loginResponse = Map.of(
            "user", user,
            "accessToken", "mock-token"
        );

        when(authService.login("test@example.com", "pass123")).thenReturn(Optional.of(loginResponse));

        AuthController.LoginRequest req = new AuthController.LoginRequest();
        req.setEmail("test@example.com");
        req.setPassword("pass123");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.email").value("test@example.com"))
            .andExpect(jsonPath("$.accessToken").value("mock-token"));
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        when(authService.login("wrong@example.com", "wrongpass")).thenReturn(Optional.empty());

        AuthController.LoginRequest req = new AuthController.LoginRequest();
        req.setEmail("wrong@example.com");
        req.setPassword("wrongpass");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }
}
