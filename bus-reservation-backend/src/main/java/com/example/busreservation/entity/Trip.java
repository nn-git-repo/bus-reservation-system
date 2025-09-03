package com.example.busreservation.entity;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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

@Entity @Table(name="trips")
public class Trip {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    private Bus bus;

    @ManyToOne(optional=false)
    private Route route;

    private Instant departureTime;
    private Instant arrivalTime;
    private double baseFare;

    @Enumerated(EnumType.STRING)
    private TripStatus status = TripStatus.OPEN;
    
    // One trip can have many bookings
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Booking> bookings;

    public Trip() {}
    // getters/setters
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Bus getBus(){return bus;} public void setBus(Bus bus){this.bus=bus;}
    public Route getRoute(){return route;} public void setRoute(Route route){this.route=route;}
    public Instant getDepartureTime(){return departureTime;} public void setDepartureTime(Instant departureTime){this.departureTime=departureTime;}
    public Instant getArrivalTime(){return arrivalTime;} public void setArrivalTime(Instant arrivalTime){this.arrivalTime=arrivalTime;}
    public double getBaseFare(){return baseFare;} public void setBaseFare(double baseFare){this.baseFare=baseFare;}
    public TripStatus getStatus(){return status;} public void setStatus(TripStatus status){this.status=status;}

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}
