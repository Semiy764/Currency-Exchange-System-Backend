package org.example.service.impl;

import org.example.exception.ResourceNotFoundException;
import org.example.model.Teller;
import org.example.repository.interfaces.TellerRepository;
import org.example.service.interfaces.TellerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public class TellerServiceImpl implements TellerService {

    private final TellerRepository tellerRepository;

    public TellerServiceImpl(TellerRepository tellerRepository) {
        this.tellerRepository = tellerRepository;
    }

    @Override
    public Teller findById(int tellerId) {
        if(tellerId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Enter a valid number for tellerId"
            );
        }
        Teller teller = tellerRepository.findById(tellerId);
        if(teller == null) {
            throw new ResourceNotFoundException("Teller not found");
        }
        return teller;
    }
}
