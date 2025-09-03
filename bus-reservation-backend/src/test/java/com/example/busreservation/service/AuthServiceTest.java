package com.example.busreservation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.busreservation.entity.Role;
import com.example.busreservation.entity.User;
import com.example.busreservation.repository.UserRepository;
import com.example.busreservation.util.JwtUtil;

class AuthServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private JwtUtil jwt;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        String name = "John";
        String email = "john@example.com";
        String pass = "password";
        String phone = "1234567890";

        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());
        when(encoder.encode(pass)).thenReturn("encodedPass");

        User savedUser = new User();
        savedUser.setName(name);
        savedUser.setEmail(email);
        savedUser.setPhone(phone);
        savedUser.setPassword("encodedPass");
        savedUser.setRole(Role.USER);

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        User result = authService.register(name, email, pass, phone);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        assertEquals("encodedPass", result.getPassword());
        assertEquals(Role.USER, result.getRole());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        String email = "existing@example.com";
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(new User()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> 
            authService.register("John", email, "pass", "123")
        );
        assertEquals("Email already registered", ex.getMessage());
    }

    @Test
    void testLogin_Success() {
        String email = "user@example.com";
        String pass = "password";

        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPass");
        user.setName("John");
        user.setRole(Role.USER);

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(encoder.matches(pass, "encodedPass")).thenReturn(true);
        when(jwt.generateToken(eq(email), anyMap())).thenReturn("mockToken");

        Optional<Map<String, Object>> result = authService.login(email, pass);

        assertTrue(result.isPresent());
        Map<String, Object> res = result.get();
        assertNotNull(res.get("user"));
        assertEquals("mockToken", res.get("accessToken"));
    }

    @Test
    void testLogin_WrongPassword() {
        String email = "user@example.com";
        String pass = "wrong";

        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPass");

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(encoder.matches(pass, "encodedPass")).thenReturn(false);

        Optional<Map<String, Object>> result = authService.login(email, pass);
        assertTrue(result.isEmpty());
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepo.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Optional<Map<String, Object>> result = authService.login("notfound@example.com", "pass");
        assertTrue(result.isEmpty());
    }

    @Test
    void testByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = authService.byEmail("test@example.com");
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }
}
