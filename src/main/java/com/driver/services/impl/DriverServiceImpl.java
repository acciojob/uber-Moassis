package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.*;
import com.driver.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements DriverService {

	@Autowired
	DriverRepository driverRepository3;

	@Autowired
	CabRepository cabRepository3;

	@Override
	public void register(String mobile, String password) {
		// Save a driver in the database having given details and a cab with ratePerKm
		// as 10 and availability as True by default.
		Driver driver = new Driver();
		driver.setMobile(mobile);
		driver.setPassword(password);

		Cab cab = new Cab();
		cab.setAvailable(true);
		cab.setPerKmRate(10);
		cab.setDriver(driver);

		driver.setCab(cab);
		driverRepository3.save(driver);
	}

	@Override
	public void removeDriver(int driverId) {
		// Delete driver without using deleteById function
		Driver driver = driverRepository3.findById(driverId).get();
		if (driver == null) {
			return;
		}
		driverRepository3.delete(driver);

		Cab cab = driver.getCab();
		cabRepository3.delete(cab);
	}

	@Override
	public void updateStatus(int driverId) {
		// Set the status of respective car to unavailable
		Driver driver = driverRepository3.findById(driverId).get();
		Cab cab = driver.getCab();
		cab.setAvailable(false);
		cabRepository3.save(cab);

	}
}
