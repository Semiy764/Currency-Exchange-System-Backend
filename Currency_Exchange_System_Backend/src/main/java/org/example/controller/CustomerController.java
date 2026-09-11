package org.example.controller;

import org.example.model.Customer;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.CustomerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public Customer getCustomer(@PathVariable int id) {
        return customerService.findById(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public Customer searchByNationalCode(@RequestParam(required = true) String nationalCode) {
        return customerService.findByNationalId(nationalCode);
    }

    @GetMapping("/me")
    public Customer getMyProfile(@AuthenticationPrincipal AuthenticatedUser principal) {
        return customerService.findByUserId(principal.id());
    }
}
