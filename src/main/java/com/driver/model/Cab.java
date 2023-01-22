package com.driver.model;

import javax.persistence.*;

@Entity
public class Cab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int perKmRate;

    boolean available;

    @OneToOne(mappedBy = "cab", cascade = CascadeType.ALL)
    Driver driver;

    public Cab(int cabId, int perKmRate, boolean availale, Driver driver) {
        this.id = cabId;
        this.perKmRate = perKmRate;
        this.available = availale;
        this.driver = driver;
    }

    public Cab() {
    }

    public int getId() {
        return id;
    }

    public void setId(int cabId) {
        this.id = cabId;
    }

    public int getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(int perKmRate) {
        this.perKmRate = perKmRate;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean availale) {
        this.available = availale;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

}
