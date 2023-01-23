package com.driver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.driver.model.Cab;
import com.driver.repository.CabRepository;

@Service
public class CabService {

    @Autowired
    CabRepository cabRepository;

    public Cab createCab() {
        Cab cab = new Cab();
        cab.setAvailable(true);
        cab.setPerKmRate(10);
        cabRepository.save(cab);
        return cab;
    }

}
