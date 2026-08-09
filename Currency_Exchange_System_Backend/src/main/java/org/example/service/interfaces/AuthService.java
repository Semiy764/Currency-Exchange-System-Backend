package org.example.service.interfaces;

import org.example.dto.request.RegisterRequest;
import org.example.dto.response.AuthResponse;
import org.example.model.User;

public interface AuthService {

    User register(RegisterRequest request);
    User login(String username, String password);
    void changePassword(int userId, String oldPassword, String newPassword);

}
