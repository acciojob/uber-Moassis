package com.driver.services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.driver.model.*;
import com.driver.repository.*;
@Service
public class TripBookingService {

    @Autowired
    TripBookingRepository tripBookingRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CabRepository cabRepository;

    @Autowired
    DriverRepository driverRepository;

    public TripBooking bookTrip(Integer customerId, String fromLocation, String toLocation, Integer distanceInKm) {

        TripBooking tripBooking = new TripBooking();
        Driver driver = null;
        Cab cab = null;
        int bill = 0;

        // check whether cab is available or not
        List<Cab> cabs = cabRepository.findAll();
        ListIterator<Cab> itr = cabs.listIterator();
        while (itr.hasNext()) {
            cab = itr.next();
            if (cab.isAvailable()) {
                cab.setAvailable(false);
                driver = cab.getDriver();
                break;
            }
        }

        if (cab == null) {
            return null;
        } else {
            tripBooking.setDriver(driver);

            bill = distanceInKm * (cab.getPerKmRate());
            tripBooking.setBill(bill);
        }

        Customer customer = customerRepository.findById(customerId).get();

        tripBooking.setCustomer(customer);
        tripBooking.setDistanceInKm(distanceInKm);
        tripBooking.setFromLocation(fromLocation);
        tripBooking.setToLocation(toLocation);
        tripBooking.setStatus(TripStatus.CONFIRMED);

        // Saving in repository
        tripBookingRepository.save(tripBooking);

        return tripBooking;
    }

    public void completeTrip(Integer tripId) {

        // for tripBooking Repository
        TripBooking tripBooking = tripBookingRepository.findById(tripId).get();
        if (tripBooking == null) {
            return;
        }

        tripBooking.setStatus(TripStatus.COMPLETED);

        // for Driver Repository
        Driver driver = tripBooking.getDriver();
        List<TripBooking> tripBookingListOfDriver = driver.getTripBookingList();
        if (tripBookingListOfDriver == null) {
            tripBookingListOfDriver = new ArrayList<>();
        }
        tripBookingListOfDriver.add(tripBooking);
        driver.setTripBookingList(tripBookingListOfDriver);
        driverRepository.save(driver);

        // for Customer Repository
        Customer customer = tripBooking.getCustomer();
        List<TripBooking> tripBookingListOfCustomer = customer.getTripBookingList();
        if (tripBookingListOfCustomer == null) {
            tripBookingListOfCustomer = new ArrayList<>();
        }
        tripBookingListOfCustomer.add(tripBooking);
        customer.setTripBookingList(tripBookingListOfCustomer);
        customerRepository.save(customer);

        // for cab Repository
        Cab cab = driver.getCab();
        cab.setAvailable(true);
        cabRepository.save(cab);
    }

    public void cancelTrip(Integer tripId) {
        TripBooking tripBooking = tripBookingRepository.findById(tripId).get();

        if (tripBooking == null) {
            return;
        }
        // for cab Repository
        Driver driver = tripBooking.getDriver();
        Cab cab = driver.getCab();
        cab.setAvailable(true);
        cabRepository.save(cab);

        // for tripBooking Repository
        tripBooking.setStatus(TripStatus.CANCELED);
        tripBookingRepository.save(tripBooking);
    }
}
