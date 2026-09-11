package org.example.service.impl;

import org.example.exception.DuplicateResourceException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Teller;
import org.example.repository.interfaces.TellerRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.TellerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TellerServiceImpl implements TellerService {

    private static final String PHONE_PATTERN = "^(\\+98|0)9\\d{9}$";
    private static final String NATIONAL_ID_PATTERN = "^\\d{10}$";

    private final TellerRepository tellerRepository;
    private final UserRepsitory userRepsitory;

    public TellerServiceImpl(TellerRepository tellerRepository, UserRepsitory userRepsitory) {
        this.tellerRepository = tellerRepository;
        this.userRepsitory = userRepsitory;
    }

    @Override
    public Teller findById(int tellerId) {
        if(tellerId <= 0) {
            throw new IllegalArgumentException("Please enter a valid teller id");
        }
        Teller teller = tellerRepository.findById(tellerId);
        if(teller == null) {
            throw new ResourceNotFoundException("Teller not found");
        }
        return teller;
    }

    @Override
    public List<Teller> findAll() {
        return tellerRepository.findAll();
    }

    @Override
    public boolean existsById(int tellerId) {

        if(tellerId <= 0) {
            throw new IllegalArgumentException("Please enter a valid teller id");
        }
        return tellerRepository.existsById(tellerId);
    }

    @Override
    @Transactional
    public Teller updateTeller(int tellerId, Teller teller) {

        if (tellerId <= 0) {
            throw new IllegalArgumentException("teller id must be positive");
        }

        if (teller == null) {
            throw new IllegalArgumentException("teller cannot be null");
        }

        if (teller.getId() == null || teller.getId().intValue() != tellerId) {
            throw new IllegalArgumentException("teller id in path does not matches teller id in request body");
        }

        Teller foundTeller = tellerRepository.findById(tellerId);
        if (foundTeller == null) {
            throw new ResourceNotFoundException("Teller not found with ID: " + tellerId);
        }

        if (teller.getFullname() == null || teller.getFullname().isBlank()) {
            throw new IllegalArgumentException("teller full name is required");
        }

        teller.setFullname(teller.getFullname().trim());
        if (teller.getFullname().length() > 30) {
            throw new IllegalArgumentException("teller name must not exceed 30 characters");
        }

        if (teller.getNationalId() == null || teller.getNationalId().isBlank()) {
            throw new IllegalArgumentException("teller national id is required");
        }

        if (teller.getUserId() == null || teller.getUserId() <= 0) {
            throw new IllegalArgumentException("Enter a valid number for teller id");
        }

        if (teller.getPhoneNumber() == null || teller.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("Please enter a valid phone number");
        }


        teller.setPhoneNumber(teller.getPhoneNumber().trim());
        teller.setNationalId(teller.getNationalId().trim());

        if (!teller.getPhoneNumber().matches(PHONE_PATTERN)) {
            throw new IllegalArgumentException("Phone number does not matches the pattern");
        }

        if (!teller.getNationalId().matches(NATIONAL_ID_PATTERN)) {
            throw new IllegalArgumentException("National id must be a 10-digit number");
        }

        int userId = teller.getUserId().intValue();
        if (!userRepsitory.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        Teller existingByUserId = tellerRepository.findByUserId(teller.getUserId().intValue());
        if (existingByUserId != null && existingByUserId.getId() != null &&
                existingByUserId.getId().intValue() != tellerId) {
            throw new DuplicateResourceException("This user id already has been associated to this teller");
        }

        Teller existingByPhoneNumber = tellerRepository.findByPhone(teller.getPhoneNumber());
        if (existingByPhoneNumber != null && existingByPhoneNumber.getId() != null &&
                existingByPhoneNumber.getId().intValue() != tellerId) {
            throw new DuplicateResourceException("This phone number has been saved");
        }

        Teller existingByNationalId = tellerRepository.findByNationalId(teller.getNationalId());
        if (existingByNationalId != null && existingByNationalId.getId() != null &&
                existingByNationalId.getId().intValue() != tellerId) {
            throw new DuplicateResourceException("This national id has already been saved");
        }

        return tellerRepository.update(teller);
    }

    @Override
    public Teller findByUserId(int userId) {

        if(userId <= 0) {
            throw new IllegalArgumentException("Please enter a valid user id");
        }
        Teller teller = tellerRepository.findByUserId(userId);
        if(teller == null) {
            throw new ResourceNotFoundException("teller not found");
        }
        return teller;
    }
}
