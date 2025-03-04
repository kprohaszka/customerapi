package com.example.customerapi.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testValidCustomer() {
        Customer customer = createValidCustomer();

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testCustomerConstructor() {
        Customer customer = new Customer(
                "John",
                "Doe",
                "john.doe@example.com",
                LocalDate.of(1990, 1, 1),
                "+1234567890"
        );

        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("john.doe@example.com", customer.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), customer.getDateOfBirth());
        assertEquals("+1234567890", customer.getPhoneNumber());
    }

    @Test
    void testCustomerGettersAndSetters() {
        Customer customer = new Customer();
        UUID id = UUID.randomUUID();

        customer.setId(id);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        customer.setPhoneNumber("+1234567890");

        assertEquals(id, customer.getId());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("john.doe@example.com", customer.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), customer.getDateOfBirth());
        assertEquals("+1234567890", customer.getPhoneNumber());
    }

    @Test
    void testFirstNameValidation() {
        Customer customer = createValidCustomer();
        customer.setFirstName(null);

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    void testLastNameValidation() {
        Customer customer = createValidCustomer();
        customer.setLastName(null);

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void testEmailValidation() {
        Customer customer = createValidCustomer();
        customer.setEmail("invalid-email");

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testDateOfBirthValidation() {
        Customer customer = createValidCustomer();
        customer.setDateOfBirth(LocalDate.now().plusDays(1)); // Future date

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dateOfBirth")));
    }

    @Test
    void testPhoneNumberValidation() {
        Customer customer = createValidCustomer();
        customer.setPhoneNumber("invalid-phone");

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber")));
    }

    private Customer createValidCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        customer.setPhoneNumber("+1234567890");
        return customer;
    }
}