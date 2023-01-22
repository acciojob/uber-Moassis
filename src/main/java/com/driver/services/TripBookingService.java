package com.driver.services;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Cab;
import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.model.TripBooking;
import com.driver.model.TripStatus;
import com.driver.repository.CabRepository;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

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

        Customer customer = customerRepository.findById(customerId).get();

        TripBooking tripBooking = new TripBooking();
        tripBooking.setCustomer(customer);
        tripBooking.setDistanceInKm(distanceInKm);
        tripBooking.setFromLocation(fromLocation);
        tripBooking.setToLocation(toLocation);
        tripBooking.setStatus(TripStatus.CONFIRMED);

        Driver driver = null;
        Cab cab = null;
        int bill = 0;

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

        tripBooking.setDriver(driver);

        if (cab != null) {
            bill = distanceInKm * (cab.getPerKmRate());
        }
        tripBooking.setBill(bill);

        // Saving in repository
        tripBookingRepository.save(tripBooking);

        return tripBooking;
    }

    public void completeTrip(Integer tripId) {

        // for tripBooking Repository
        TripBooking tripBooking = tripBookingRepository.findById(tripId).get();
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
