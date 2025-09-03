package com.example.busreservation.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity @Table(name="bookings")
public class Booking {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JsonBackReference // prevents recursion back to Booking
    private User user;

    @ManyToOne(optional=false)
    @JsonBackReference
    private Trip trip;

    private Instant bookingTime = Instant.now();
    private double totalAmount;

    private String pnr;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.HELD;

    /** hold TTL: if now > holdExpiresAt and still HELD => invalid */
    private Instant holdExpiresAt;

    @OneToMany(mappedBy="booking", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonBackReference
    private List<BookingSeat> seats = new ArrayList<>();

    public Booking() {}
    // getters/setters
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public User getUser(){return user;} public void setUser(User user){this.user=user;}
    public Trip getTrip(){return trip;} public void setTrip(Trip trip){this.trip=trip;}
    public Instant getBookingTime(){return bookingTime;} public void setBookingTime(Instant bookingTime){this.bookingTime=bookingTime;}
    public double getTotalAmount(){return totalAmount;} public void setTotalAmount(double totalAmount){this.totalAmount=totalAmount;}
    public String getPnr(){return pnr;} public void setPnr(String pnr){this.pnr=pnr;}
    public BookingStatus getStatus(){return status;} public void setStatus(BookingStatus status){this.status=status;}
    public Instant getHoldExpiresAt(){return holdExpiresAt;} public void setHoldExpiresAt(Instant holdExpiresAt){this.holdExpiresAt=holdExpiresAt;}
    public List<BookingSeat> getSeats(){return seats;} public void setSeats(List<BookingSeat> seats){this.seats=seats;}
}
