package com.example.busreservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.busreservation.entity.Booking;
import com.example.busreservation.entity.BookingStatus;
import com.example.busreservation.entity.Payment;
import com.example.busreservation.entity.PaymentStatus;
import com.example.busreservation.repository.BookingRepository;
import com.example.busreservation.repository.PaymentRepository;

@Service
public class PaymentService {
    private final PaymentRepository payRepo;
    private final BookingRepository bookingRepo;

    public PaymentService(PaymentRepository payRepo, BookingRepository bookingRepo){
        this.payRepo = payRepo; this.bookingRepo = bookingRepo;
    }

    /** Mock payment: always succeeds, then confirm booking */
    @Transactional
    public Payment checkout(Long bookingId) {
        Booking b = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        Payment p = new Payment();
        p.setBooking(b);
        p.setAmount(b.getTotalAmount());
        p.setStatus(PaymentStatus.SUCCESS);
        payRepo.save(p);

        b.setStatus(BookingStatus.CONFIRMED);
        bookingRepo.save(b);
        return p;
    }
}
