package com.example.busreservation.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.busreservation.entity.Trip;
import com.example.busreservation.repository.TripRepository;

@Service
public class TripService {
    private final TripRepository tripRepo;
    public TripService(TripRepository tripRepo){ this.tripRepo = tripRepo; }

    public List<Trip> search(String src, String dst, LocalDate date) {
        ZonedDateTime start = date.atStartOfDay(ZoneId.of("UTC"));
        ZonedDateTime end   = start.plusDays(1).minusSeconds(1);
        return tripRepo.findByRoute_SourceAndRoute_DestinationAndDepartureTimeBetween(
                src, dst, start.toInstant(), end.toInstant());
    }
}
