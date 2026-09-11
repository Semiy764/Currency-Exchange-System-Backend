package org.example.controller;

import org.example.model.Teller;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.TellerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/tellers")
public class TellerController {

    private final TellerService tellerService;

    public TellerController(TellerService tellerService) {
        this.tellerService = tellerService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Teller> findAllTellers() {
        return tellerService.findAll();
    }

    @GetMapping("/{tellerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Teller findTeller(@PathVariable int tellerId) {
        return tellerService.findById(tellerId);
    }

    @GetMapping("/me")
    public Teller getMyProfile(@AuthenticationPrincipal AuthenticatedUser principal) {
        return tellerService.findByUserId(principal.id());
    }

}
