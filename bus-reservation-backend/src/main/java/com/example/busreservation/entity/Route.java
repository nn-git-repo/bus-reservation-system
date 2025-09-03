package com.example.busreservation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name="routes")
public class Route {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String source;
    private String destination;
    private int distanceKm;
    private int durationMin;

    public Route() {}
    // getters/setters
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getSource(){return source;} public void setSource(String source){this.source=source;}
    public String getDestination(){return destination;} public void setDestination(String destination){this.destination=destination;}
    public int getDistanceKm(){return distanceKm;} public void setDistanceKm(int distanceKm){this.distanceKm=distanceKm;}
    public int getDurationMin(){return durationMin;} public void setDurationMin(int durationMin){this.durationMin=durationMin;}
}
