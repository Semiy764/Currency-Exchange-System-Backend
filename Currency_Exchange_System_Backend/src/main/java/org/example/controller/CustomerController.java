package org.example.controller;

import org.example.model.Customer;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping
    public List<Customer> getAllCustomers(@AuthenticationPrincipal AuthenticatedUser principal) {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable int id,
                            @AuthenticationPrincipal AuthenticatedUser principal) {
        isAdminOrTeller(principal);
        return customerService.findById(id);
    }

    @GetMapping("/search")
    public Customer searchByNationalCode(@RequestParam(required = true) String nationalCode,
                                         @AuthenticationPrincipal AuthenticatedUser principal) {

        isAdminOrTeller(principal);
        return customerService.findByNationalId(nationalCode);
    }


    private void isAdminOrTeller(AuthenticatedUser principal) {

        if(!"ADMIN".equals(principal.role()) && !"TELLER".equals(principal.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin or Teller only");
        }
    }


}
