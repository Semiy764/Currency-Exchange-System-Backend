package org.example.controller;

import org.example.dto.request.UserUpdateRequest;
import org.example.dto.response.UserResponse;
import org.example.model.User;
import org.example.service.interfaces.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> findAll() {
        return userService
                .findAll()
                .stream()
                .map(this :: map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public UserResponse getUserById(@PathVariable int id) {
        return map(userService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse userinfoChange(@RequestBody UserUpdateRequest infos,
                                       @PathVariable int id) {


        User user = userService.findById(id);
        if (infos.getUsername() == null || infos.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (infos.getActive() == null) {
            throw new IllegalArgumentException("Is active is required");
        }

        user.setUsername(infos.getUsername());
        user.setActive(infos.getActive());
        userService.updateUser(id, user);
        return map(user);
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse deactivateUser(@PathVariable int id) {
        userService.deactivateUser(id);
        return map(userService.findById(id));
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse activateUser(@PathVariable int id) {

        userService.activateUser(id);
        return map(userService.findById(id));
    }

    private UserResponse map(User user) {

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setActive(user.isActive());
        userResponse.setUserRole(user.getRole().name());

        return userResponse;
    }

}
