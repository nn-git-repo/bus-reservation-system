package com.example.busreservation.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.busreservation.entity.Trip;
import com.example.busreservation.service.TripService;

@RestController @RequestMapping("/api/v1/trips")
public class TripController {
    private final TripService tripService;
    public TripController(TripService tripService){ this.tripService = tripService; }

    @GetMapping("/search")
    public List<Trip> search(@RequestParam String source,
                             @RequestParam String destination,
                             @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date){
        return tripService.search(source, destination, date);
    }
}
