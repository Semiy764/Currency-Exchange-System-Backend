package org.example.service.impl;

import org.example.dto.request.RegisterRequest;
import org.example.dto.response.AuthResponse;
import org.example.enums.UserRole;
import org.example.model.Customer;
import org.example.model.Teller;
import org.example.model.User;
import org.example.repository.interfaces.CustomerRepository;
import org.example.repository.interfaces.TellerRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepsitory userRepsitory;
    private final CustomerRepository customerRepository;
    private final TellerRepository tellerRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthServiceImpl(UserRepsitory userRepsitory, CustomerRepository customerRepository, TellerRepository tellerRepository, PasswordEncoder passwordEncoder) {
        this.userRepsitory = userRepsitory;
        this.customerRepository = customerRepository;
        this.tellerRepository = tellerRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User register(RegisterRequest request) {

        if(userRepsitory.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "this username already exists"
                    );
        }

        UserRole role = request.getRole();

        if(role == null) {
            request.setRole(UserRole.CUSTOMER);
            role = UserRole.CUSTOMER;
        }

        if(role == UserRole.CUSTOMER && customerRepository.existsByPhone(request.getPhone())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "this phone number already exists"
            );
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setActive(true);

        User saved = userRepsitory.save(user);

        if(role == UserRole.CUSTOMER) {
            Customer customer = new Customer();
            customer.setUserId(saved.getId());
            customer.setNationalId(request.getNationalId());
            customer.setFullname(request.getFullName());
            customer.setPhoneNumber(request.getPhone());
            customerRepository.save(customer);
        }

        if(role == UserRole.TELLER) {
            Teller teller = new Teller();
            teller.setUserId(saved.getId());
            teller.setNationalId(request.getNationalId());
            teller.setFullname(request.getFullName());
            teller.setPhoneNumber(request.getPhone());
            tellerRepository.save(teller);
        }

        return saved;

    }

    @Override
    public User login(String username, String password) {

        User user = userRepsitory.findByUsername(username);

        if(user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password"
            );
        }

        if(!user.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Account is deactivated. Please contact support"
            );
        }

        return user;
    }
}
