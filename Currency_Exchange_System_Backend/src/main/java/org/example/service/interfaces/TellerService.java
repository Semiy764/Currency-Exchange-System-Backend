package org.example.service.interfaces;

import org.example.model.Teller;

import java.util.List;

public interface TellerService {

    Teller findById(int tellerId);
    List<Teller> findAll();
}
