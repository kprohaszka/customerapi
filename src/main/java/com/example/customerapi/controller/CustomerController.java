package com.example.customerapi.controller;

import com.example.customerapi.model.Customer;
import com.example.customerapi.service.CustomerService;
import com.example.customerapi.exception.CustomerNotFoundException;
import com.example.customerapi.exception.InvalidCustomerDataException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Customer Management", description = "Operations for customer management including CRUD operations and statistics")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Creates a new customer with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "400", description = "Invalid customer data provided"),
            @ApiResponse(responseCode = "409", description = "Customer with this email already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createCustomer(
            @Parameter(description = "Customer object to be created", required = true)
            @Valid @RequestBody Customer customer) {
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
    @Operation(summary = "Get a customer by ID", description = "Returns a customer based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customer",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<?> getCustomerById(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable UUID id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (CustomerNotFoundException e) {
            return createErrorResponse("Customer not found", e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @Operation(summary = "Get all customers", description = "Returns a list of all customers")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved customer list",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class)))
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a customer", description = "Updates a customer based on the provided ID with new details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "400", description = "Invalid customer data provided"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "409", description = "Customer with this email already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> updateCustomer(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated customer object", required = true)
            @Valid @RequestBody Customer customer) {
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
    @Operation(summary = "Delete a customer", description = "Deletes a customer based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<?> deleteCustomer(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable UUID id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (CustomerNotFoundException e) {
            return createErrorResponse("Customer not found", e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/average-age")
    @Operation(summary = "Get average customer age", description = "Returns the average age of all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated average age",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "500", description = "Failed to calculate average age")
    })
    public ResponseEntity<?> getAverageAge() {
        try {
            double averageAge = customerService.getAverageAge();
            return ResponseEntity.ok(averageAge);
        } catch (RuntimeException e) {
            return createErrorResponse("Failed to calculate average age", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/age-range")
    @Operation(summary = "Get customers within age range", description = "Returns customers with ages between the specified minimum and maximum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customers",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "400", description = "Invalid age range parameters"),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve customers")
    })
    public ResponseEntity<?> getCustomersBetweenAges(
            @Parameter(description = "Minimum age", required = true)
            @RequestParam() Integer minAge,
            @Parameter(description = "Maximum age", required = true)
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