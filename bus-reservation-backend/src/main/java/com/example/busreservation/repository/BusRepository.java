package com.example.busreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.busreservation.entity.Bus;

public interface BusRepository extends JpaRepository<Bus, Long> {
}
