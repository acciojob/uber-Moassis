package com.driver.services.impl;

import com.driver.model.*;
import com.driver.services.*;
import com.driver.repository.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Autowired
	TripBookingService tripBookingService;

	@Autowired
	CabRepository cabRepository;

	@Override
	public void register(Customer customer) {
		// Save the customer in database
		customer.setMobile(customer.getMobile());
		customer.setPassword(customer.getPassword());
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer = customerRepository2.findById(customerId).get();
		if (customer != null) {
			customerRepository2.delete(customer);
		}
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm)
			throws Exception {
		// Book the driver with lowest driverId who is free (cab available variable is
		// Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		// Avoid using SQL query
		TripBooking tripBooking = new TripBooking();
		Driver driver = null;
		Cab cab = null;
		int bill = 0;

		// check whether driver is available or not
		List<Driver> drivers = driverRepository2.findAll();
		ListIterator<Driver> itr = drivers.listIterator();
		while (itr.hasNext()) {
			driver = itr.next();
			cab = driver.getCab();
			if (cab.isAvailable()) {
				cab.setAvailable(false);
				break;
			}
		}

		if (cab == null) {
			throw new Exception("No cab available!");
		} else {
			tripBooking.setDriver(driver);

			bill = distanceInKm * (cab.getPerKmRate());
			tripBooking.setBill(bill);
		}

		Customer customer = customerRepository2.findById(customerId).get();

		tripBooking.setCustomer(customer);
		tripBooking.setDistanceInKm(distanceInKm);
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setStatus(TripStatus.CONFIRMED);

		// Saving in repository
		tripBookingRepository2.save(tripBooking);

		return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId) {
		// Cancel the trip having given trip Id and update TripBooking attributes
		// accordingly
		// for tripBooking Repository
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();

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
		tripBooking.setBill(0);
		tripBookingRepository2.save(tripBooking);

	}

	@Override
	public void completeTrip(Integer tripId) {
		// Complete the trip having given trip Id and update TripBooking attributes
		// accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		if (tripBooking == null) {
			return;
		}

		tripBooking.setStatus(TripStatus.COMPLETED);
		tripBookingRepository2.save(tripBooking);

		// for Driver Repository
		Driver driver = tripBooking.getDriver();
		List<TripBooking> tripBookingListOfDriver = driver.getTripBookingList();
		if (tripBookingListOfDriver == null) {
			tripBookingListOfDriver = new ArrayList<>();
		}
		tripBookingListOfDriver.add(tripBooking);
		driver.setTripBookingList(tripBookingListOfDriver);
		driverRepository2.save(driver);

		// for Customer Repository
		Customer customer = tripBooking.getCustomer();
		List<TripBooking> tripBookingListOfCustomer = customer.getTripBookingList();
		if (tripBookingListOfCustomer == null) {
			tripBookingListOfCustomer = new ArrayList<>();
		}
		tripBookingListOfCustomer.add(tripBooking);
		customer.setTripBookingList(tripBookingListOfCustomer);
		customerRepository2.save(customer);

		// for cab Repository
		Cab cab = driver.getCab();
		cab.setAvailable(true);
		cabRepository.save(cab);
	}
}
