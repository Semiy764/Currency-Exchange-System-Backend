package org.example.service.interfaces;

import org.example.model.User;

public interface UserService {

    User findById(int userId);
    User findByUsername(String username);

}
