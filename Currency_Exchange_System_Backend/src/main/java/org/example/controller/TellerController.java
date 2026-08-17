package org.example.controller;

import org.example.exception.ResourceNotFoundException;
import org.example.model.Teller;
import org.example.repository.interfaces.TellerRepository;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.TellerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/tellers")
public class TellerController {

    private final TellerService tellerService;

    public TellerController(TellerService tellerService) {
        this.tellerService = tellerService;
    }

    @GetMapping
    public List<Teller> findAllTellers(@AuthenticationPrincipal AuthenticatedUser principal) {

        isAdmin(principal);
        return tellerService.findAll();
    }

    private void isAdmin(AuthenticatedUser principal) {

        if(!"ADMIN".equals(principal.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin only");
        }
    }
}
