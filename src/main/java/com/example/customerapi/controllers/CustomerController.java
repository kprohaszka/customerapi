package com.example.customerapi.controllers;

import com.example.customerapi.models.Customer;
import com.example.customerapi.services.CustomerService;
import com.example.customerapi.exceptions.CustomerNotFoundException;
import com.example.customerapi.exceptions.InvalidCustomerDataException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer) {
        try {
            Customer createdCustomer = customerService.createCustomer(customer);
            return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("customers_email_key")) {
                return createErrorResponse("Duplicate email", "A customer with this email already exists", HttpStatus.CONFLICT);
            }
            return createErrorResponse("Database error", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InvalidCustomerDataException e) {
            return createErrorResponse("Invalid customer data", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable UUID id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (CustomerNotFoundException e) {
            return createErrorResponse("Customer not found", e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable UUID id, @Valid @RequestBody Customer customer) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(id, customer);
            return ResponseEntity.ok(updatedCustomer);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("customers_email_key")) {
                return createErrorResponse("Duplicate email", "A customer with this email already exists", HttpStatus.CONFLICT);
            }
            return createErrorResponse("Database error", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (CustomerNotFoundException e) {
            return createErrorResponse("Customer not found", e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidCustomerDataException e) {
            return createErrorResponse("Invalid customer data", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable UUID id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (CustomerNotFoundException e) {
            return createErrorResponse("Customer not found", e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/average-age")
    public ResponseEntity<?> getAverageAge() {
        try {
            double averageAge = customerService.getAverageAge();
            return ResponseEntity.ok(averageAge);
        } catch (RuntimeException e) {
            return createErrorResponse("Failed to calculate average age", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/age-range")
    public ResponseEntity<?> getCustomersBetweenAges(
            @RequestParam() Integer minAge,
            @RequestParam() Integer maxAge) {
        if (minAge == null || maxAge == null) {
            return createErrorResponse("Invalid parameters", "Both minAge and maxAge are required", HttpStatus.BAD_REQUEST);
        }
        if (minAge > maxAge) {
            return createErrorResponse("Invalid age range", "minAge must be less than or equal to maxAge", HttpStatus.BAD_REQUEST);
        }
        try {
            List<Customer> customers = customerService.getCustomersBetweenAges(minAge, maxAge);
            return ResponseEntity.ok(customers);
        } catch (RuntimeException e) {
            return createErrorResponse("Failed to retrieve customers", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> createErrorResponse(String error, String message, HttpStatus status) {
        Map<String, String> response = new HashMap<>();
        response.put("error", error);
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }
}