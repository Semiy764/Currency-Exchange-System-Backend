package org.example.controller;

import org.example.dto.request.UserUpdateRequest;
import org.example.dto.response.UserResponse;
import org.example.model.User;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
    public List<UserResponse> findAll(@AuthenticationPrincipal AuthenticatedUser principal) {
        requireAdmin(principal);
        return userService
                .findAll()
                .stream()
                .map(this :: map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@AuthenticationPrincipal AuthenticatedUser principal,
                                    @PathVariable int id) {
        requireAdminOrTeller(principal);
        return map(userService.findById(id));
    }

    @PutMapping("/{id}")
    public UserResponse userinfoChange(@RequestBody UserUpdateRequest infos,
                                       @PathVariable int id,
                                       @AuthenticationPrincipal AuthenticatedUser principal) {

        requireAdmin(principal);
        User user = userService.findById(id);
        if (infos.getUsername() == null || infos.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        if (infos.getActive() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Is active is required");
        }

        user.setUsername(infos.getUsername());
        user.setActive(infos.getActive());
        userService.updateUser(id, user);
        return map(user);
    }

    @PostMapping("/{id}/deactivate")
    public UserResponse deactivateUser(@PathVariable int id,
                                       @AuthenticationPrincipal AuthenticatedUser principal) {
        requireAdmin(principal);
        userService.deactivateUser(id);
        return map(userService.findById(id));
    }

    @PostMapping("/{id}/activate")
    public UserResponse activateUser(@PathVariable int id,
                                     @AuthenticationPrincipal AuthenticatedUser principal) {

        requireAdmin(principal);
        userService.activateUser(id);
        return map(userService.findById(id));
    }

    private void requireAdmin(AuthenticatedUser principal) {
        if(!"ADMIN".equals(principal.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admins only");
        }
    }

    private void requireAdminOrTeller(AuthenticatedUser principal) {
            if(!"ADMIN".equals(principal.role()) && !"TELLER".equals(principal.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin or Teller only");
        }
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
