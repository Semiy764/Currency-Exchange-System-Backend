package org.example.dto.request;

import org.example.enums.UserRole;

public class CustomerRegisterRequest extends RegisterRequest {

    public CustomerRegisterRequest(String fullName, String nationalId, String password, String phone, UserRole role, String username) {
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.username = username;
    }
}
