package com.example.busreservation.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.busreservation.entity.Bus;
import com.example.busreservation.entity.Route;
import com.example.busreservation.entity.Trip;
import com.example.busreservation.entity.TripStatus;
import com.example.busreservation.repository.BusRepository;
import com.example.busreservation.repository.RouteRepository;
import com.example.busreservation.repository.TripRepository;

@RestController
@RequestMapping("/api/v1/admin/trips")
public class AdminTripController {
    private final TripRepository tripRepo;
    private final BusRepository busRepo;
    private final RouteRepository routeRepo;

    public AdminTripController(TripRepository tripRepo, BusRepository busRepo, RouteRepository routeRepo) {
        this.tripRepo = tripRepo;
        this.busRepo = busRepo;
        this.routeRepo = routeRepo;
    }

    // 🔹 Get all trips
    @GetMapping
    public List<Trip> all() {
        return tripRepo.findAll();
    }

    // 🔹 Create new trip
    @PostMapping
    public ResponseEntity<?> create(@RequestBody TripRequest req) {
        return ResponseEntity.ok(saveOrUpdateTrip(new Trip(), req));
    }

    // 🔹 Update existing trip
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TripRequest req) {
        return tripRepo.findById(id)
                .map(existing -> ResponseEntity.ok(saveOrUpdateTrip(existing, req)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Delete trip
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return tripRepo.findById(id)
                .map(trip -> {
                    tripRepo.delete(trip);
                    return ResponseEntity.ok("Trip deleted successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Reusable save/update logic
    private Trip saveOrUpdateTrip(Trip trip, TripRequest req) {
        Bus bus = busRepo.findById(req.busId)
                .orElseThrow(() -> new RuntimeException("Bus not found"));
        Route route = routeRepo.findById(req.routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        trip.setBus(bus);
        trip.setRoute(route);
        trip.setDepartureTime(Instant.parse(req.departureTime));
        trip.setArrivalTime(Instant.parse(req.arrivalTime));
        trip.setBaseFare(req.baseFare);

        if (trip.getStatus() == null) {
            trip.setStatus(TripStatus.OPEN);
        }

        return tripRepo.save(trip);
    }

    // 🔹 DTO class for requests
    public static class TripRequest {
        public Long busId;
        public Long routeId;
        public String departureTime;
        public String arrivalTime;
        public double baseFare;
    }
}
