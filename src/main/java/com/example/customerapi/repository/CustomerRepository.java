package com.example.customerapi.repository;

import com.example.customerapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query("SELECT COALESCE(AVG(YEAR(CURRENT_DATE) - YEAR(c.dateOfBirth)), 0) FROM Customer c")
    Double findAverageAge();
}
