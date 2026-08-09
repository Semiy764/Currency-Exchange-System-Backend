package org.example.service.impl;

import org.example.exception.ResourceNotFoundException;
import org.example.model.User;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepsitory userRepsitory;

    public UserServiceImpl(UserRepsitory userRepsitory) {
        this.userRepsitory = userRepsitory;
    }


    @Override
    public User findById(int userId) {

        User user = userRepsitory.findById(userId);
        if(user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        return user;
    }
}
