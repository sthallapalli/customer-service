package com.example.customer.persistence.repository;

import com.example.customer.persistence.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface CustomerRepository extends  JpaRepository<Customer, Long> {

    Optional<Customer> findByFirstName(String firstName);

}
