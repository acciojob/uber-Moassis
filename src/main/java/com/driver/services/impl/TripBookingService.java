package com.driver.services.impl;

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
    CustomerServiceImpl customerServiceImpl;

    @Autowired
    DriverRepository driverRepository;

    public TripBooking bookTrip(Integer customerId, String fromLocation, String toLocation, Integer distanceInKm)
            throws Exception {
        TripBooking tripBooking = customerServiceImpl.bookTrip(customerId, fromLocation, toLocation, distanceInKm);
        return tripBooking;
    }

    public void completeTrip(Integer tripId) {
        customerServiceImpl.completeTrip(tripId);
    }

    public void cancelTrip(Integer tripId) {
        customerServiceImpl.cancelTrip(tripId);
    }
}
