package com.example.busreservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.busreservation.entity.Booking;
import com.example.busreservation.service.BookingService;
import com.example.busreservation.util.CurrentUser;

@RestController @RequestMapping("/api/v1/bookings")
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService){ this.bookingService = bookingService; }

    @GetMapping("/my")
    public ResponseEntity<?> myBookings() {
        String email = CurrentUser.email();
        return ResponseEntity.ok(bookingService.findBookingsForUserSimple(email));
    }

    @PostMapping("/hold")
    public ResponseEntity<?> hold(@RequestBody HoldRequest req){
        String email = CurrentUser.email();
        Booking b = bookingService.holdSeats(req.tripId, req.seats, email);
        return ResponseEntity.ok(b);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirm(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.confirm(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.cancelWithRefund(id));
    }

    @GetMapping("/{tripId}/taken-seats")
    public ResponseEntity<?> taken(@PathVariable Long tripId){
        return ResponseEntity.ok(bookingService.currentTakenSeats(tripId));
    }

    public static class HoldRequest {
        public Long tripId; public List<String> seats;
    }
}
