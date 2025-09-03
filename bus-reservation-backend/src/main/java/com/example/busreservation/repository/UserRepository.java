package com.example.busreservation.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.busreservation.entity.User;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
