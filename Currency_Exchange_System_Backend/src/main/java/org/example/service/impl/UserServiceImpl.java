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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepsitory userRepsitory;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepsitory userRepsitory, CustomerRepository customerRepository, TellerRepository tellerRepository, PasswordEncoder passwordEncoder) {
        this.userRepsitory = userRepsitory;

        this.passwordEncoder = passwordEncoder;
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

    @Override
    public List<User> findByRole(UserRole role) {
        List<User> users = userRepsitory.findByRole(role);
        return users;
    }

    @Override
    public List<User> findActiveUsers() {
        List<User> users = userRepsitory.findActiveUsers();
        return users;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepsitory.existsByUsername(username);
    }

    @Override
    public User updateUser(int userId, User user) {

        if(userId != user.getId()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "user id in path does not match user id in request body"
            );
        }

        User existing = userRepsitory.findById(userId);
        if(existing == null) {
            throw new ResourceNotFoundException("user not found with ID: " + userId);
        }

        return userRepsitory.update(user);
    }

    @Override
    public void deactivateUser(int userId) {

        User user = userRepsitory.findById(userId);
        if(user == null) {
            throw new ResourceNotFoundException("User not found with userId: " + userId);
        }

        user.setActive(false);
        userRepsitory.update(user);

    }

    @Override
    public void activateUser(int userId) {

        User user = userRepsitory.findById(userId);
        if(user == null) {
            throw new ResourceNotFoundException("User not found with userId: " + userId);
        }

        user.setActive(true);
        userRepsitory.update(user);
    }

    @Override
    public void resetPassword(int userId, String newPassword) { // this method is just usable by admin!!!

        if(newPassword == null || newPassword.length() < 8) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "new password must be at least 8 characters"
            );
        }

        User user = userRepsitory.findById(userId);
        if(user == null) {
            throw new ResourceNotFoundException("User not found with userId: " + userId);
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepsitory.update(user);
    }
}
