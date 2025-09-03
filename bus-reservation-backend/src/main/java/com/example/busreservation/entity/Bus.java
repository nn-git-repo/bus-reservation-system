package com.example.busreservation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name="buses")
public class Bus {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String busNumber;
    private String operatorName;
    private int totalSeats;
    @Enumerated(EnumType.STRING)
    private BusType busType = BusType.AC;

    public Bus() {}
    // getters/setters
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getBusNumber(){return busNumber;} public void setBusNumber(String busNumber){this.busNumber=busNumber;}
    public String getOperatorName(){return operatorName;} public void setOperatorName(String operatorName){this.operatorName=operatorName;}
    public int getTotalSeats(){return totalSeats;} public void setTotalSeats(int totalSeats){this.totalSeats=totalSeats;}
    public BusType getBusType(){return busType;} public void setBusType(BusType busType){this.busType=busType;}
}
