package com.driver.model;

import javax.persistence.*;

@Entity
public class Cab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int cabId;

    int perKmRate;

    boolean availale;

    @OneToOne(mappedBy = "cab", cascade = CascadeType.ALL)
    Driver driver;

    public Cab(int cabId, int perKmRate, boolean availale, Driver driver) {
        this.cabId = cabId;
        this.perKmRate = perKmRate;
        this.availale = availale;
        this.driver = driver;
    }

    public Cab() {
    }

    public int getCabId() {
        return cabId;
    }

    public void setCabId(int cabId) {
        this.cabId = cabId;
    }

    public int getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(int perKmRate) {
        this.perKmRate = perKmRate;
    }

    public boolean isAvailale() {
        return availale;
    }

    public void setAvailale(boolean availale) {
        this.availale = availale;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

}
