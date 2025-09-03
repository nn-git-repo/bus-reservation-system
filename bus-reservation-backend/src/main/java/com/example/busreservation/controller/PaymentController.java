package com.example.busreservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.busreservation.entity.Payment;
import com.example.busreservation.service.PaymentService;

@RestController @RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService){ this.paymentService = paymentService; }

    @PostMapping("/checkout/{bookingId}")
    public ResponseEntity<Payment> checkout(@PathVariable Long bookingId){
        return ResponseEntity.ok(paymentService.checkout(bookingId));
    }
}
