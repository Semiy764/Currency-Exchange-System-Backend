package org.example.model;

import org.example.enums.UserRole;


// user is for auhtorizatiob but customer has supply informations of users and
// this is separation of concnerns

public class User {

    private long id;
    private String username;
    private String passwordHash;
    private UserRole role;
    private boolean isActive;

    public User(Long id, boolean isActive, String passwordHash, UserRole role, String username) {
        this.id = id;
        this.isActive = isActive;
        this.passwordHash = passwordHash;
        this.role = role;
        this.username = username;
    }

    public User(String username, String passwordHash, UserRole role, boolean isActive) {
        this.isActive = isActive;
        this.passwordHash = passwordHash;
        this.role = role;
        this.username = username;
    }

    public User() {}

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
