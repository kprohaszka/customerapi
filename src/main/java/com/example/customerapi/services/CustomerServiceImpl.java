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


}
