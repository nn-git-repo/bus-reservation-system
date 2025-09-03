package com.example.busreservation.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.busreservation.entity.Booking;
import com.example.busreservation.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTripIdAndStatusIn(Long tripId, List<BookingStatus> statuses);
    
    List<Booking> findByUserEmail(String email);
  
}
