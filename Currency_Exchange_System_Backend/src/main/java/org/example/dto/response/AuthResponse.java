package org.example.dto.response;

import org.example.enums.UserRole;

import javax.management.relation.Role;

public class AuthResponse {

    private String token;
    private Long userId;
    private String username;
    private UserRole role;

    public AuthResponse() {}
    public AuthResponse(UserRole role, String token, Long userId, String username) {
        this.role = role;
        this.token = token;
        this.userId = userId;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
