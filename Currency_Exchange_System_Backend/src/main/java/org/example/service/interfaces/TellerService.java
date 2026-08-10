package org.example.service.interfaces;

import org.example.model.Teller;

import java.util.List;

public interface TellerService {

    Teller findById(int tellerId);
    List<Teller> findAll();
    boolean existsById(int tellerId);
    Teller updateTeller(int tellerId, Teller teller);
    Teller findByUserId(int userId);
}
