package com.example.customerapi.services;

import com.example.customerapi.models.Customer;
import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer getCustomerById(Long id);
    List<Customer> getAllCustomers();
    Customer updateCustomer(Long id, Customer customer);
    void deleteCustomer(Long id);
    double getAverageAge();
    List<Customer> getCustomersBetweenAges(int minAge, int maxAge);
}