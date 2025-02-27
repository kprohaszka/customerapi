package com.example.customerapi.repositories;

import com.example.customerapi.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // For my custom query methods if needed.
}
