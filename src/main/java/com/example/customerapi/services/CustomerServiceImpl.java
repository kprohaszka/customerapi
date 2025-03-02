package com.example.customerapi.services;

import com.example.customerapi.models.Customer;
import com.example.customerapi.repositories.CustomerRepository;
import com.example.customerapi.exceptions.CustomerNotFoundException;
import com.example.customerapi.exceptions.InvalidCustomerDataException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        validateCustomer(customer);
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = getCustomerById(id);
        validateCustomer(customer);
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setDateOfBirth(customer.getDateOfBirth());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    public double getAverageAge() {
        return customerRepository.findAverageAge();
    }

    @Override
    public List<Customer> getCustomersBetweenAges(int minAge, int maxAge) {
        LocalDate now = LocalDate.now();
        return customerRepository.findAll().stream()
                .filter(customer -> {
                    int age = Period.between(customer.getDateOfBirth(), now).getYears();
                    return minAge <= age && age <= maxAge;
                })
                .collect(Collectors.toList());
    }

    private void validateCustomer(Customer customer) {
        if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty()) {
            throw new InvalidCustomerDataException("First name is required");
        }
        if (customer.getLastName() == null || customer.getLastName().trim().isEmpty()) {
            throw new InvalidCustomerDataException("Last name is required");
        }
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new InvalidCustomerDataException("Email is required");
        }
        if (customer.getDateOfBirth() == null) {
            throw new InvalidCustomerDataException("Date of birth is required");
        }
        if (customer.getPhoneNumber() == null || customer.getPhoneNumber().trim().isEmpty()) {
            throw new InvalidCustomerDataException("Phone number is required");
        }
    }
}