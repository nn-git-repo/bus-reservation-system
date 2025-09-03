package com.example.busreservation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.busreservation.entity.Booking;
import com.example.busreservation.entity.BookingStatus;
import com.example.busreservation.entity.Payment;
import com.example.busreservation.entity.PaymentStatus;
import com.example.busreservation.repository.BookingRepository;
import com.example.busreservation.repository.PaymentRepository;

class PaymentServiceTest {

    @Mock
    private PaymentRepository payRepo;

    @Mock
    private BookingRepository bookingRepo;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckout_Success() {
        // Mock booking
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setTotalAmount(500.0);
        booking.setStatus(BookingStatus.HELD);

        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));
        when(payRepo.save(any(Payment.class))).thenAnswer(i -> i.getArgument(0));
        when(bookingRepo.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Payment payment = paymentService.checkout(1L);

        assertNotNull(payment);
        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
        assertEquals(500.0, payment.getAmount());

        // Booking should be confirmed
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());

        verify(payRepo, times(1)).save(any(Payment.class));
        verify(bookingRepo, times(1)).save(booking);
    }

    @Test
    void testCheckout_BookingNotFound() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentService.checkout(1L));
        assertEquals("Booking not found", ex.getMessage());
    }
}
