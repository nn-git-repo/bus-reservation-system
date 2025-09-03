package com.example.busreservation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.busreservation.entity.Booking;
import com.example.busreservation.entity.BookingSeat;
import com.example.busreservation.entity.BookingStatus;
import com.example.busreservation.entity.Bus;
import com.example.busreservation.entity.Route;
import com.example.busreservation.entity.Trip;
import com.example.busreservation.entity.User;
import com.example.busreservation.repository.BookingRepository;
import com.example.busreservation.repository.BookingSeatRepository;
import com.example.busreservation.repository.TripRepository;
import com.example.busreservation.repository.UserRepository;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private BookingSeatRepository seatRepo;

    @Mock
    private TripRepository tripRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHoldSeats_Success() {
        // Nested entities
        Bus bus = new Bus();
        bus.setId(1L);
        bus.setBusNumber("BUS123");

        Route route = new Route();
        route.setId(1L);
        route.setSource("CityA");
        route.setDestination("CityB");

        Trip trip = new Trip();
        trip.setId(1L);
        trip.setBaseFare(100.0);
        trip.setBus(bus);
        trip.setRoute(route);

        User user = new User();
        user.setEmail("test@example.com");

        // Mocks
        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(bookingRepo.findByTripIdAndStatusIn(anyLong(), anyList())).thenReturn(List.of());
        when(bookingRepo.save(any(Booking.class))).thenAnswer(i -> {
            Booking b = i.getArgument(0);
            b.setSeats(List.of()); // Initialize seats to avoid NPE
            return b;
        });
        when(seatRepo.save(any(BookingSeat.class))).thenAnswer(i -> i.getArgument(0));

        Booking booking = bookingService.holdSeats(1L, List.of("A1", "A2"), "test@example.com");

        assertNotNull(booking);
        assertEquals(BookingStatus.HELD, booking.getStatus());
        assertEquals(200.0, booking.getTotalAmount());
    }

    @Test
    void testConfirm_Success() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(BookingStatus.HELD);
        booking.setHoldExpiresAt(Instant.now().plusSeconds(600));

        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepo.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking confirmed = bookingService.confirm(1L);

        assertEquals(BookingStatus.CONFIRMED, confirmed.getStatus());
    }

    @Test
    void testCancelWithRefund_Success() {
        // Trip departing in 12 hours → 50% refund
        Trip trip = new Trip();
        trip.setDepartureTime(Instant.now().plusSeconds(3600*12)); // 12 hours later

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setTotalAmount(1000.0);
        booking.setPnr("ABC123");
        booking.setTrip(trip);

        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepo.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        var res = bookingService.cancelWithRefund(1L);

        assertEquals("ABC123", res.get("pnr"));
        assertEquals(0.5, res.get("refundPercent"));
        assertEquals(500.0, res.get("refundAmount"));
    }

    @Test
    void testCurrentTakenSeats() {
        BookingSeat seat = new BookingSeat();
        seat.setSeatNo("A1");

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setSeats(List.of(seat));

        when(bookingRepo.findByTripIdAndStatusIn(1L, List.of(BookingStatus.HELD, BookingStatus.CONFIRMED)))
            .thenReturn(List.of(booking));

        var seats = bookingService.currentTakenSeats(1L);

        assertEquals(1, seats.size());
        assertEquals("A1", seats.get(0));
    }

    @Test
    void testFindBookingsForUserSimple() {
        Bus bus = new Bus();
        bus.setId(1L);
        bus.setBusNumber("BUS123");

        Route route = new Route();
        route.setId(1L);
        route.setSource("CityA");
        route.setDestination("CityB");

        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDepartureTime(Instant.now().plusSeconds(3600));
        trip.setArrivalTime(Instant.now().plusSeconds(7200));
        trip.setBaseFare(100.0);
        trip.setBus(bus);
        trip.setRoute(route);

        BookingSeat seat = new BookingSeat();
        seat.setSeatNo("A1");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setPnr("PNR123");
        booking.setStatus(BookingStatus.HELD);
        booking.setTotalAmount(100.0);
        booking.setBookingTime(Instant.now());
        booking.setHoldExpiresAt(Instant.now().plusSeconds(600));
        booking.setTrip(trip);
        booking.setSeats(List.of(seat));

        when(bookingRepo.findByUserEmail("user@example.com")).thenReturn(List.of(booking));

        var list = bookingService.findBookingsForUserSimple("user@example.com");

        assertEquals(1, list.size());
        var map = list.get(0);
        assertEquals("PNR123", map.get("pnr"));
        assertEquals(BookingStatus.HELD, map.get("status"));
        assertEquals(100.0, map.get("totalAmount"));
        List<String> seats = (List<String>) map.get("seats");
        assertEquals(1, seats.size());
        assertEquals("A1", seats.get(0));
    }
}
