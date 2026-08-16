package org.example.dto.response;

import org.example.enums.UserRole;
import org.example.model.User;
import org.example.repository.interfaces.UserRepsitory;

public class UserResponse {

    private Long id;
    private String username;
    private boolean isActive;
    private String userRole;


    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
