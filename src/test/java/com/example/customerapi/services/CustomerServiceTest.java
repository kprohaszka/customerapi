package com.example.customerapi.services;

import com.example.customerapi.BaseTest;
import com.example.customerapi.exceptions.CustomerNotFoundException;
import com.example.customerapi.exceptions.InvalidCustomerDataException;
import com.example.customerapi.models.Customer;
import com.example.customerapi.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
class CustomerServiceTest extends BaseTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void testCreateCustomer_DuplicateEmail() {
        Customer customer1 = createValidCustomer();
        customerService.createCustomer(customer1);

        Customer customer2 = createValidCustomer();
        customer2.setEmail(customer1.getEmail());

        assertThrows(DataIntegrityViolationException.class, () -> customerService.createCustomer(customer2));
    }

    @Test
    void testUpdateCustomer_DuplicateEmail() {
        Customer customer1 = createValidCustomer();
        customerService.createCustomer(customer1);

        Customer customer2 = createValidCustomer();
        customer2.setEmail("unique@example.com");
        Customer savedCustomer2 = customerService.createCustomer(customer2);

        savedCustomer2.setEmail(customer1.getEmail());
        assertThrows(DataIntegrityViolationException.class, () -> customerService.updateCustomer(savedCustomer2.getId(), savedCustomer2));
    }

    @Test
    void testCreateCustomer() {
        Customer customer = createValidCustomer();
        Customer savedCustomer = customerService.createCustomer(customer);

        assertNotNull(savedCustomer.getId());
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName());
        assertEquals(customer.getLastName(), savedCustomer.getLastName());
        assertEquals(customer.getEmail(), savedCustomer.getEmail());
        assertEquals(customer.getDateOfBirth(), savedCustomer.getDateOfBirth());
        assertEquals(customer.getPhoneNumber(), savedCustomer.getPhoneNumber());
    }

    @Test
    void testCreateCustomer_InvalidData() {
        Customer customer = new Customer();
        assertThrows(InvalidCustomerDataException.class, () -> customerService.createCustomer(customer));
    }

    @Test
    void testGetCustomerById() {
        Customer customer = createValidCustomer();
        Customer savedCustomer = customerService.createCustomer(customer);

        Customer retrievedCustomer = customerService.getCustomerById(savedCustomer.getId());
        assertEquals(savedCustomer.getId(), retrievedCustomer.getId());
    }

    @Test
    void testGetCustomerById_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(nonExistentId));
    }

    @Test
    void testGetAllCustomers() {
        customerService.createCustomer(createValidCustomer());
        customerService.createCustomer(createValidCustomer());

        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(2, customers.size());
    }

    @Test
    void testUpdateCustomer() {
        Customer customer = createValidCustomer();
        Customer savedCustomer = customerService.createCustomer(customer);

        savedCustomer.setFirstName("UpdatedFirstName");
        savedCustomer.setEmail("updated@example.com");

        Customer updatedCustomer = customerService.updateCustomer(savedCustomer.getId(), savedCustomer);

        assertEquals("UpdatedFirstName", updatedCustomer.getFirstName());
        assertEquals("updated@example.com", updatedCustomer.getEmail());
    }

    @Test
    void testUpdateCustomer_NotFound() {
        Customer customer = createValidCustomer();
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(nonExistentId, customer));
    }

    @Test
    void testDeleteCustomer() {
        Customer customer = createValidCustomer();
        Customer savedCustomer = customerService.createCustomer(customer);

        customerService.deleteCustomer(savedCustomer.getId());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(savedCustomer.getId()));
    }

    @Test
    void testDeleteCustomer_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(nonExistentId));
    }

    @Test
    void testGetAverageAge() {
        Customer customer1 = createValidCustomer();
        customer1.setDateOfBirth(LocalDate.now().minusYears(30));
        customerService.createCustomer(customer1);

        Customer customer2 = createValidCustomer();
        customer2.setDateOfBirth(LocalDate.now().minusYears(40));
        customerService.createCustomer(customer2);

        double averageAge = customerService.getAverageAge();
        assertEquals(35.0, averageAge, 0.1);
    }

    @Test
    void testGetCustomersBetweenAges() {
        Customer customer1 = createValidCustomer();
        customer1.setDateOfBirth(LocalDate.now().minusYears(25));
        customerService.createCustomer(customer1);

        Customer customer2 = createValidCustomer();
        customer2.setDateOfBirth(LocalDate.now().minusYears(35));
        customerService.createCustomer(customer2);

        Customer customer3 = createValidCustomer();
        customer3.setDateOfBirth(LocalDate.now().minusYears(45));
        customerService.createCustomer(customer3);

        List<Customer> customers = customerService.getCustomersBetweenAges(30, 40);
        assertEquals(1, customers.size());
    }

    private Customer createValidCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail(UUID.randomUUID() + "@example.com");
        customer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        customer.setPhoneNumber("+1234567890");
        return customer;
    }
}