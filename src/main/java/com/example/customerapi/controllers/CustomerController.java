package com.example.customerapi.controllers;

import com.example.customerapi.models.Customer;
import com.example.customerapi.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.createCustomer(customer), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAge() {
        return ResponseEntity.ok(customerService.getAverageAge());
    }

    @GetMapping("/age-range")
    public ResponseEntity<?> getCustomersBetweenAges(
            @RequestParam(required = true) Integer minAge,
            @RequestParam(required = true) Integer maxAge) {
        if (minAge == null || maxAge == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Both minAge and maxAge are required");
        }
        if (minAge > maxAge) {
            return ResponseEntity
                    .badRequest()
                    .body("minAge must be less than or equal to maxAge");
        }
        return ResponseEntity.ok(customerService.getCustomersBetweenAges(minAge, maxAge));
    }
}