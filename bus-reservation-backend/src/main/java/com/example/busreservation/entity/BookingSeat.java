package com.example.busreservation.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="booking_seats",
       uniqueConstraints=@UniqueConstraint(columnNames={"trip_id","seatNo"}))
public class BookingSeat {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JsonBackReference
    private Booking booking;

    @ManyToOne(optional=false)
    @JoinColumn(name="trip_id")
    private Trip trip;

    private String seatNo; // e.g. "1A"

    public BookingSeat() {}
    // getters/setters
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Booking getBooking(){return booking;} public void setBooking(Booking booking){this.booking=booking;}
    public Trip getTrip(){return trip;} public void setTrip(Trip trip){this.trip=trip;}
    public String getSeatNo(){return seatNo;} public void setSeatNo(String seatNo){this.seatNo=seatNo;}
}
