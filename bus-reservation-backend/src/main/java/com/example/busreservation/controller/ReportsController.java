package com.example.busreservation.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.busreservation.entity.Booking;
import com.example.busreservation.entity.BookingStatus;
import com.example.busreservation.repository.BookingRepository;

// DTO class to return enriched report data
class BookingReportDTO {
    public Long id;
    public String bookingTime;
    public Double totalAmount;
    public String pnr;
    public BookingStatus status;
    public String holdExpiresAt;
    public String userName;
    public String source;
    public String destination;

    public BookingReportDTO(Booking booking) {
        this.id = booking.getId();
        this.bookingTime = booking.getBookingTime() != null ? booking.getBookingTime().toString() : null;
        this.totalAmount = booking.getTotalAmount();
        this.pnr = booking.getPnr();
        this.status = booking.getStatus();
        this.holdExpiresAt = booking.getHoldExpiresAt() != null ? booking.getHoldExpiresAt().toString() : null;
        this.userName = booking.getUser() != null ? booking.getUser().getName() : null;
        this.source = booking.getTrip() != null && booking.getTrip().getRoute() != null
                ? booking.getTrip().getRoute().getSource()
                : null;
        this.destination = booking.getTrip() != null && booking.getTrip().getRoute() != null
                ? booking.getTrip().getRoute().getDestination()
                : null;
    }
}

@RestController
@RequestMapping("/api/v1/admin/reports") // Make sure frontend calls /api/v1/admin/reports
public class ReportsController {

    private final BookingRepository bookingRepository;

    public ReportsController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // 🔹 Get all bookings as a report with user and trip info
    @GetMapping
    public ResponseEntity<List<BookingReportDTO>> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();

        List<BookingReportDTO> report = bookings.stream()
                .map(BookingReportDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(report);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(BookingStatus.CANCELLED); // mark as cancelled
                    bookingRepository.save(booking);
                    return ResponseEntity.ok("Booking cancelled successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
