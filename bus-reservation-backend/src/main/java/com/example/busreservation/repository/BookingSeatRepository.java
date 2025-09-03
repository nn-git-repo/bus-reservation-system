package com.example.busreservation.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.busreservation.entity.BookingSeat;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
    List<BookingSeat> findByTripId(Long tripId);
}
