package org.example.service.impl;

import org.example.enums.UserRole;
import org.example.exception.ResourceNotFoundException;
import org.example.model.User;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// yadet bashe naghshe user ha ro dar layer service ham ba prequthorized check koni!!!!
@Service
public class UserServiceImpl implements UserService {

    private final UserRepsitory userRepsitory;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepsitory userRepsitory, PasswordEncoder passwordEncoder) {
        this.userRepsitory = userRepsitory;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User findById(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException("Please enter a valid user id");
        }
        User user = userRepsitory.findById(userId);
        if(user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Enter a valid username");
        }

        User user = userRepsitory.findByUsername(username);
        if(user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepsitory.findAll();
    }

    @Override
    public List<User> findByRole(UserRole role) {

        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        return userRepsitory.findByRole(role);

    }

    @Override
    public List<User> findActiveUsers() {
        return userRepsitory.findActiveUsers();
    }

    @Override
    public boolean existsByUsername(String username) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Enter a valid username");
        }
        return userRepsitory.existsByUsername(username);
    }

    @Override
    @Transactional
    public User updateUser(int userId, User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getId() == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }

        if (userId <= 0) {
            throw new IllegalArgumentException("User in must be positive");
        }

        if(userId != user.getId().intValue()) {
            throw new IllegalArgumentException("User id in path does not match user id in request body");
        }

        User existing = userRepsitory.findById(userId);
        if(existing == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }



        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (user.getRole() == null) {
            throw new IllegalArgumentException("User role is required");
        }

        existing.setUsername(user.getUsername());
        existing.setRole(user.getRole());
        return userRepsitory.update(existing);
    }

    @Override
    public void deactivateUser(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException("user id must be positive");
        }
        User user = userRepsitory.findById(userId);
        if(user == null) {
            throw new ResourceNotFoundException("User not found with userId: " + userId);
        }

        user.setActive(false);
        userRepsitory.update(user);

    }

    @Override
    public void activateUser(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException("user id must be positive");
        }
        User user = userRepsitory.findById(userId);
        if(user == null) {
            throw new ResourceNotFoundException("User not found with userId: " + userId);
        }

        user.setActive(true);
        userRepsitory.update(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void resetPassword(int userId, String newPassword) { // this method is just usable by admin!!!

        if(newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("new password must be at least 8 characters");
        }

        User user = userRepsitory.findById(userId);
        if(user == null) {
            throw new ResourceNotFoundException("User not found with userId: " + userId);
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepsitory.update(user);
    }

    @Override
    public boolean isActive(int userId) {

        if(userId <= 0) {
            throw new IllegalArgumentException("user id must be positive");
        }

        User user = userRepsitory.findById(userId);
        if(user == null) {
            throw new ResourceNotFoundException("user not found with id: " + userId);
        }
        return user.isActive();
    }


}
