package com.example.customerapi.services;

import com.example.customerapi.models.Customer;
import com.example.customerapi.repositories.CustomerRepository;
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
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("The customer is not found"));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = getCustomerById(id);
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setDateOfBirth(customer.getDateOfBirth());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public double getAverageAge() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .mapToInt(customer -> Period.between(customer.getDateOfBirth(), LocalDate.now()).getYears())
                .average()
                .orElse(0);
    }

    @Override
    public List<Customer> getCustomersBetweenAges(int minAge, int maxAge) {
        LocalDate now = LocalDate.now();
        return customerRepository.findAll().stream()
                .filter(customer -> {
                    int age = Period.between(customer.getDateOfBirth(), now).getYears();
                    return age >= minAge && age <= maxAge;
                })
                .collect(Collectors.toList());
    }
}
