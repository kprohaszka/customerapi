package com.example.customerapi.service;

import com.example.customerapi.model.Customer;
import java.util.List;
import java.util.UUID;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer getCustomerById(UUID id);
    List<Customer> getAllCustomers();
    Customer updateCustomer(UUID id, Customer customer);
    void deleteCustomer(UUID id);
    double getAverageAge();
    List<Customer> getCustomersBetweenAges(int minAge, int maxAge);
}