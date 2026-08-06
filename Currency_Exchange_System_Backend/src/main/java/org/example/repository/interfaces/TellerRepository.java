package org.example.repository.interfaces;

import org.example.model.Customer;
import org.example.model.Teller;

import java.util.List;

public interface TellerRepository {

    Teller save(Teller teller);

//    Teller update(Teller teller);
//
    void delete(int tellerId);

    void deleteByUserId(int userId);
//
    Teller findByUserId(int tellerId);

    List<Teller> findAll();

    Teller findById(int tellerId);

    boolean existsByUserId(int userId);

    boolean existsById(int tellerId);

    boolean existsByPhone(String phone);
}
