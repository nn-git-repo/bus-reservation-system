package com.example.busreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.busreservation.entity.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
