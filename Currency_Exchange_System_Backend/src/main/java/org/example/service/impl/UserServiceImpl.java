package org.example.service.impl;

import org.example.enums.UserRole;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Teller;
import org.example.model.User;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.repository.interfaces.CustomerRepository;
import org.example.repository.interfaces.TellerRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepsitory userRepsitory;
    private final CustomerRepository customerRepository;
    private final TellerRepository tellerRepository;

    public UserServiceImpl(UserRepsitory userRepsitory, CustomerRepository customerRepository, TellerRepository tellerRepository) {
        this.userRepsitory = userRepsitory;
        this.customerRepository = customerRepository;
        this.tellerRepository = tellerRepository;
    }


    @Override
    public User findById(int userId) {

        User user = userRepsitory.findById(userId);
        if(user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {

        User user = userRepsitory.findByUsername(username);

        if(user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> allUsers = userRepsitory.findAll();
        return allUsers;
    }

//    @Override
//    public List<User> findByRole(UserRole role) {
//
//    }
}
