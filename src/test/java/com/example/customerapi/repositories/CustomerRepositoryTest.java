package com.example.customerapi.repositories;

import com.example.customerapi.BaseTest;
import com.example.customerapi.models.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends BaseTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void testSaveCustomer() {
        Customer customer = createValidCustomer();
        Customer savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer.getId());
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName());
        assertEquals(customer.getLastName(), savedCustomer.getLastName());
    }

    @Test
    void testFindById() {
        Customer customer = createValidCustomer();
        Customer savedCustomer = customerRepository.save(customer);

        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());
        assertTrue(foundCustomer.isPresent());
        assertEquals(savedCustomer.getId(), foundCustomer.get().getId());
    }

    @Test
    void testFindAll() {
        customerRepository.save(createValidCustomer());
        customerRepository.save(createValidCustomer());

        List<Customer> customers = customerRepository.findAll();
        assertEquals(2, customers.size());
    }

    @Test
    void testDeleteById() {
        Customer customer = createValidCustomer();
        Customer savedCustomer = customerRepository.save(customer);

        customerRepository.deleteById(savedCustomer.getId());

        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());
        assertFalse(foundCustomer.isPresent());
    }

    @Test
    void testFindAverageAge() {
        Customer customer1 = createValidCustomer();
        customer1.setDateOfBirth(LocalDate.now().minusYears(30));
        customerRepository.save(customer1);

        Customer customer2 = createValidCustomer();
        customer2.setDateOfBirth(LocalDate.now().minusYears(40));
        customerRepository.save(customer2);

        Double averageAge = customerRepository.findAverageAge();
        assertEquals(35.0, averageAge, 0.1);
    }

    @Test
    void testFindAverageAge_NoCustomers() {
        Double averageAge = customerRepository.findAverageAge();
        assertEquals(0.0, averageAge);
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