package org.example.service.interfaces;

import org.example.enums.UserRole;
import org.example.model.User;

import java.util.List;

public interface UserService {

    User findById(int userId);
    User findByUsername(String username);
    List<User> findAll();
    List<User> findByRole(UserRole role);
    List<User> findActiveUsers();
    boolean existsByUsername(String username);
    User updateUser(int userId, User user);
    // we dont have method for deactive user because we are doinng this in update user;

}
