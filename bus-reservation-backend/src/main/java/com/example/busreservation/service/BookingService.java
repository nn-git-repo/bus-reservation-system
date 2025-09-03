package com.example.busreservation.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.busreservation.entity.Booking;
import com.example.busreservation.entity.BookingSeat;
import com.example.busreservation.entity.BookingStatus;
import com.example.busreservation.entity.Trip;
import com.example.busreservation.entity.User;
import com.example.busreservation.repository.BookingRepository;
import com.example.busreservation.repository.BookingSeatRepository;
import com.example.busreservation.repository.TripRepository;
import com.example.busreservation.repository.UserRepository;

@Service
public class BookingService {
    private final BookingRepository bookingRepo;
    private final BookingSeatRepository seatRepo;
    private final TripRepository tripRepo;
    private final UserRepository userRepo;

    public BookingService(BookingRepository bookingRepo, BookingSeatRepository seatRepo,
                          TripRepository tripRepo, UserRepository userRepo) {
        this.bookingRepo = bookingRepo;
        this.seatRepo = seatRepo;
        this.tripRepo = tripRepo;
        this.userRepo = userRepo;
    }

    /** Seats are held for 10 minutes */
    @Transactional
    public Booking holdSeats(Long tripId, List<String> seatNos, String userEmail) {
        Trip trip = tripRepo.findById(tripId).orElseThrow(() -> new RuntimeException("Trip not found"));
        User user = userRepo.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        List<Booking> activeBookings = bookingRepo.findByTripIdAndStatusIn(tripId,
                List.of(BookingStatus.HELD, BookingStatus.CONFIRMED));

        Set<String> blocked = new HashSet<>();
        for (Booking b : activeBookings) {
            if (b.getStatus() == BookingStatus.HELD && b.getHoldExpiresAt() != null &&
                Instant.now().isAfter(b.getHoldExpiresAt())) continue;
            for (BookingSeat s : b.getSeats()) blocked.add(s.getSeatNo());
        }
        for (String s : seatNos) {
            if (blocked.contains(s)) throw new RuntimeException("Seat already taken: " + s);
        }

        Booking booking = new Booking();
        booking.setTrip(trip);
        booking.setUser(user);
        booking.setStatus(BookingStatus.HELD);
        booking.setPnr(UUID.randomUUID().toString().substring(0,6).toUpperCase());
        booking.setHoldExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES));
        booking.setTotalAmount(trip.getBaseFare() * seatNos.size());
        booking = bookingRepo.save(booking);

        for (String seat : seatNos) {
            BookingSeat bs = new BookingSeat();
            bs.setBooking(booking);
            bs.setTrip(trip);
            bs.setSeatNo(seat);
            seatRepo.save(bs);
        }
        return booking;
    }

    @Transactional
    public Booking confirm(Long bookingId) {
        Booking b = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (b.getStatus() != BookingStatus.HELD) throw new RuntimeException("Booking not in HELD state");
        if (b.getHoldExpiresAt() != null && Instant.now().isAfter(b.getHoldExpiresAt()))
            throw new RuntimeException("Hold expired");
        b.setStatus(BookingStatus.CONFIRMED);
        return bookingRepo.save(b);
    }

    /** Simple time-based refund policy */
    @Transactional
    public Map<String,Object> cancelWithRefund(Long bookingId) {
        Booking b = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (b.getStatus() == BookingStatus.CANCELLED) throw new RuntimeException("Already cancelled");
        double refundPct = computeRefundPercent(b.getTrip().getDepartureTime());
        b.setStatus(BookingStatus.CANCELLED);
        bookingRepo.save(b);
        double amount = b.getTotalAmount() * refundPct;
        Map<String,Object> res = new HashMap<>();
        res.put("pnr", b.getPnr());
        res.put("refundPercent", refundPct);
        res.put("refundAmount", amount);
        return res;
    }

    private double computeRefundPercent(Instant departure) {
        long hours = java.time.Duration.between(Instant.now(), departure).toHours();
        if (hours >= 24) return 0.8;
        if (hours >= 6) return 0.5;
        return 0.0;
    }

    /** ORIGINAL method returning Booking entities */
    public List<Booking> findBookingsForUser(String email) {
        return bookingRepo.findByUserEmail(email);
    }

    /** NEW: simplified output for frontend */
    public List<Map<String,Object>> findBookingsForUserSimple(String email) {
        List<Booking> bookings = bookingRepo.findByUserEmail(email);
        List<Map<String,Object>> result = new ArrayList<>();

        for (Booking b : bookings) {
            Map<String,Object> map = new HashMap<>();
            map.put("id", b.getId());
            map.put("pnr", b.getPnr());
            map.put("status", b.getStatus());
            map.put("totalAmount", b.getTotalAmount());
            map.put("bookingTime", b.getBookingTime());
            map.put("holdExpiresAt", b.getHoldExpiresAt());

            // Trip info
            Map<String,Object> trip = new HashMap<>();
            trip.put("id", b.getTrip().getId());
            trip.put("departureTime", b.getTrip().getDepartureTime());
            trip.put("arrivalTime", b.getTrip().getArrivalTime());
            trip.put("baseFare", b.getTrip().getBaseFare());

            Map<String,Object> bus = new HashMap<>();
            bus.put("id", b.getTrip().getBus().getId());
            bus.put("busNumber", b.getTrip().getBus().getBusNumber());
            trip.put("bus", bus);

            Map<String,Object> route = new HashMap<>();
            route.put("id", b.getTrip().getRoute().getId());
            route.put("source", b.getTrip().getRoute().getSource());
            route.put("destination", b.getTrip().getRoute().getDestination());
            trip.put("route", route);

            map.put("trip", trip);

            // Seats
            List<String> seats = new ArrayList<>();
            for (BookingSeat s : b.getSeats()) seats.add(s.getSeatNo());
            map.put("seats", seats);

            result.add(map);
        }

        return result;
    }

    public List<String> currentTakenSeats(Long tripId) {
        List<Booking> active = bookingRepo.findByTripIdAndStatusIn(tripId,
                List.of(BookingStatus.HELD, BookingStatus.CONFIRMED));
        List<String> seats = new ArrayList<>();
        for (Booking b : active) {
            if (b.getStatus() == BookingStatus.HELD && b.getHoldExpiresAt() != null &&
                Instant.now().isAfter(b.getHoldExpiresAt())) continue;
            for (BookingSeat s : b.getSeats()) seats.add(s.getSeatNo());
        }
        return seats;
    }
}
