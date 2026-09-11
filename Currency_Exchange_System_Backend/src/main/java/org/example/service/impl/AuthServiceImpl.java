package org.example.service.impl;
import org.example.dto.request.RegisterRequest;
import org.example.enums.UserRole;
import org.example.exception.AccessDeniedException;
import org.example.exception.DuplicateResourceException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Customer;
import org.example.model.Teller;
import org.example.model.User;
import org.example.repository.interfaces.CustomerRepository;
import org.example.repository.interfaces.TellerRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    @Transactional
    public User register(RegisterRequest request) {

        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("Please enter a valid username");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Please enter a valid password");
        }

        if (request.getFullName() == null || request.getFullName().isBlank()) {
            throw new IllegalArgumentException("Please enter a valid full name");
        }

        if (request.getPhone() == null || request.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone number re required");
        }

        if (request.getNationalId() == null || request.getNationalId().isBlank()) {
            throw new IllegalArgumentException("National id is required");
        }

        UserRole role = request.getRole();

        if(role == null) {
            request.setRole(UserRole.CUSTOMER);
            role = UserRole.CUSTOMER;
        }

        if(role == UserRole.CUSTOMER && customerRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("this phone number already exists");
        }


        if(userRepsitory.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("This user name already exists");
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

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Enter a valid username");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Enter a valid password");
        }
        User user = userRepsitory.findByUsername(username);

        if(user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new AccessDeniedException("Invalid username or password");
        }

        if(!user.isActive()) {
            throw new AccessDeniedException("Account is deactivated. Please contact support");
        }

        return user;
    }

    @Override
    public void changePassword(int userId, String oldPassword, String newPassword) {

        if (userId <= 0) {
            throw new IllegalArgumentException("Enter a valid user id");
        }

        if(oldPassword == null || oldPassword.isBlank()) {
            throw new IllegalArgumentException("old password is required");
        }

        if(newPassword == null || newPassword.isBlank() || newPassword.length() < 8) {
            throw new IllegalArgumentException("your new password must be at least 8 characters");
        }

         User user = userRepsitory.findById(userId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        if(!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
             throw new AccessDeniedException("old password is incorrect");
         }

         if(passwordEncoder.matches(newPassword, user.getPasswordHash())) {
             throw new AccessDeniedException("new password must be different from old password");
         }
         user.setPasswordHash(passwordEncoder.encode(newPassword));
         userRepsitory.update(user);
    }
}
