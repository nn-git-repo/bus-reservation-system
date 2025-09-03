package com.example.busreservation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.busreservation.entity.Trip;
import com.example.busreservation.repository.TripRepository;

class TripServiceTest {

    @Mock
    private TripRepository tripRepo;

    @InjectMocks
    private TripService tripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearch_ReturnsTrips() {
        String src = "CityA";
        String dst = "CityB";
        LocalDate date = LocalDate.of(2025, 9, 2);

        // Mock trips
        Trip trip1 = new Trip();
        Trip trip2 = new Trip();
        List<Trip> trips = new ArrayList<>();
        trips.add(trip1);
        trips.add(trip2);

        ZonedDateTime start = date.atStartOfDay(ZoneId.of("UTC"));
        ZonedDateTime end = start.plusDays(1).minusSeconds(1);

        when(tripRepo.findByRoute_SourceAndRoute_DestinationAndDepartureTimeBetween(
                src, dst, start.toInstant(), end.toInstant()))
            .thenReturn(trips);

        List<Trip> result = tripService.search(src, dst, date);

        assertEquals(2, result.size());
        verify(tripRepo, times(1))
            .findByRoute_SourceAndRoute_DestinationAndDepartureTimeBetween(
                    src, dst, start.toInstant(), end.toInstant());
    }

    @Test
    void testSearch_NoTrips() {
        String src = "CityX";
        String dst = "CityY";
        LocalDate date = LocalDate.of(2025, 9, 2);

        ZonedDateTime start = date.atStartOfDay(ZoneId.of("UTC"));
        ZonedDateTime end = start.plusDays(1).minusSeconds(1);

        when(tripRepo.findByRoute_SourceAndRoute_DestinationAndDepartureTimeBetween(
                src, dst, start.toInstant(), end.toInstant()))
            .thenReturn(List.of());

        List<Trip> result = tripService.search(src, dst, date);

        assertTrue(result.isEmpty());
        verify(tripRepo, times(1))
            .findByRoute_SourceAndRoute_DestinationAndDepartureTimeBetween(
                    src, dst, start.toInstant(), end.toInstant());
    }
}
