package com.example.busreservation.repository;
import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.busreservation.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByRoute_SourceAndRoute_DestinationAndDepartureTimeBetween(
            String source, String destination, Instant start, Instant end);
}
