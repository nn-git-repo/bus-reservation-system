package com.example.busreservation.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name="payments")
public class Payment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    private Booking booking;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.INITIATED;

    private Instant createdAt = Instant.now();

    public Payment() {}
    // getters/setters
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Booking getBooking(){return booking;} public void setBooking(Booking booking){this.booking=booking;}
    public double getAmount(){return amount;} public void setAmount(double amount){this.amount=amount;}
    public PaymentStatus getStatus(){return status;} public void setStatus(PaymentStatus status){this.status=status;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}
}
