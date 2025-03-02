package com.example.customerapi.repositories;

import com.example.customerapi.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query("SELECT COALESCE(AVG(CASE " +
            "WHEN (MONTH(c.dateOfBirth) > MONTH(CURRENT_DATE)) OR " +
            "     (MONTH(c.dateOfBirth) = MONTH(CURRENT_DATE) AND DAY(c.dateOfBirth) > DAY(CURRENT_DATE)) " +
            "THEN YEAR(CURRENT_DATE) - YEAR(c.dateOfBirth) - 1 " +
            "ELSE YEAR(CURRENT_DATE) - YEAR(c.dateOfBirth) " +
            "END), 0) FROM Customer c")
    Double findAverageAge();
}
