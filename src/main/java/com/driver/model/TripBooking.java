package com.driver.model;

import javax.persistence.*;

@Entity
public class TripBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tripBookingId;

    private String fromLocation;

    private String toLocation;

    private int distanceInKm;

    @Enumerated(value = EnumType.STRING)
    private TripStatus status;

    private int bill;

    @ManyToOne
    @JoinColumn
    private Customer customer;

    @ManyToOne
    @JoinColumn
    private Driver driver;

    public TripBooking(int tripBookingId, String fromLocation, String toLocation, int distanceInkm,
            TripStatus tripStatus, int bill, Customer customer, Driver diriver) {
        this.tripBookingId = tripBookingId;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.distanceInKm = distanceInkm;
        this.status = tripStatus;
        this.bill = bill;
        this.customer = customer;
        this.driver = diriver;
    }

    public TripBooking() {
    }

    public int getTripBookingId() {
        return tripBookingId;
    }

    public void setTripBookingId(int tripBookingId) {
        this.tripBookingId = tripBookingId;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public int getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(int distanceInkm) {
        this.distanceInKm = distanceInkm;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus tripStatus) {
        this.status = tripStatus;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver diriver) {
        this.driver = diriver;
    }

}
