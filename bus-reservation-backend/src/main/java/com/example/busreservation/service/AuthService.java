package com.example.busreservation.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.busreservation.entity.Role;
import com.example.busreservation.entity.User;
import com.example.busreservation.repository.UserRepository;
import com.example.busreservation.util.JwtUtil;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthService(UserRepository userRepo, BCryptPasswordEncoder encoder, JwtUtil jwt) {
        this.userRepo = userRepo; this.encoder = encoder; this.jwt = jwt;
    }

    public User register(String name, String email, String pass, String phone) {
        if (userRepo.findByEmail(email).isPresent()) throw new RuntimeException("Email already registered");
        User u = new User();
        u.setName(name); u.setEmail(email); u.setPhone(phone);
        u.setPassword(encoder.encode(pass)); u.setRole(Role.USER);
        return userRepo.save(u);
    }

    public Optional<Map<String,Object>> login(String email, String pass) {
        Optional<User> opt = userRepo.findByEmail(email);
        if (opt.isEmpty()) return Optional.empty();
        User u = opt.get();
        if (!encoder.matches(pass, u.getPassword())) return Optional.empty();
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", u.getRole().name()); claims.put("name", u.getName());
        String token = jwt.generateToken(u.getEmail(), claims);
        Map<String,Object> res = new HashMap<>();
        u.setPassword(null); res.put("user", u); res.put("accessToken", token);
        return Optional.of(res);
    }

    public Optional<User> byEmail(String email){ return userRepo.findByEmail(email); }
}
