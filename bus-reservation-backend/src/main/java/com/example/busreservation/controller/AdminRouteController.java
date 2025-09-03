package com.example.busreservation.controller;

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

import com.example.busreservation.entity.Route;
import com.example.busreservation.repository.RouteRepository;

@RestController
@RequestMapping("/api/v1/admin/routes")
public class AdminRouteController {

    private final RouteRepository repo;

    public AdminRouteController(RouteRepository repo) {
        this.repo = repo;
    }

    // ✅ Get all routes
    @GetMapping
    public List<Route> all() {
        return repo.findAll();
    }

    // ✅ Create a new route
    @PostMapping
    public ResponseEntity<Route> create(@RequestBody Route r) {
        return ResponseEntity.ok(repo.save(r));
    }

    // ✅ Update a route by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Route updatedRoute) {
        return repo.findById(id)
                .map(route -> {
                    route.setSource(updatedRoute.getSource());
                    route.setDestination(updatedRoute.getDestination());
                    route.setDistanceKm(updatedRoute.getDistanceKm());
                    route.setDurationMin(updatedRoute.getDurationMin());
                    return ResponseEntity.ok(repo.save(route));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Delete a route by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return repo.findById(id)
                .map(route -> {
                    repo.delete(route);
                    return ResponseEntity.ok("Route deleted successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
