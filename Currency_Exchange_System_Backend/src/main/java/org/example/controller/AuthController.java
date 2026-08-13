package org.example.controller;

import org.example.dto.request.CustomerRegisterRequest;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.TellerRegisterRequest;
import org.example.dto.response.AuthResponse;
import org.example.model.User;
import org.example.security.JwtUtil;
import org.example.service.interfaces.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;


    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/register/register-teller")
    public AuthResponse registerTeller(@RequestBody TellerRegisterRequest request) {
        User saved = authService.register(request);
        return buildAuthResponse(saved);
    }

    @PostMapping("/register/register-customer")
    public AuthResponse registerCustomer(@RequestBody CustomerRegisterRequest request) {
        User saved = authService.register(request);
        return buildAuthResponse(saved);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        User user = authService.login(request.getUsername(), request.getPassword());
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {

        String token = jwtUtil.generateToken(
                user.getId().intValue(),
                user.getUsername(),
                user.getRole().name()
        );

        return new AuthResponse(
                user.getRole(),
                token, user.getId(),
                user.getUsername());

    }
}
