package com.example.busreservation.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.busreservation.entity.User;
import com.example.busreservation.service.AuthService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController @RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){ this.authService = authService; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req){
        try {
            User u = authService.register(req.getName(), req.getEmail(), req.getPassword(), req.getPhone());
            u.setPassword(null);
            return ResponseEntity.ok(u);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req){
        return authService.login(req.getEmail(), req.getPassword())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(400).body(Map.of("error","Invalid credentials")));
    }

    static class RegisterRequest {
        @NotBlank private String name;
        @Email @NotBlank private String email;
        @NotBlank private String password;
        private String phone;
        public String getName(){return name;} public void setName(String name){this.name=name;}
        public String getEmail(){return email;} public void setEmail(String email){this.email=email;}
        public String getPassword(){return password;} public void setPassword(String password){this.password=password;}
        public String getPhone(){return phone;} public void setPhone(String phone){this.phone=phone;}
    }
    static class LoginRequest {
        @Email @NotBlank private String email;
        @NotBlank private String password;
        public String getEmail(){return email;} public void setEmail(String email){this.email=email;}
        public String getPassword(){return password;} public void setPassword(String password){this.password=password;}
    }
}
