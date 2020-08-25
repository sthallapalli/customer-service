package com.example.customer.service;


import com.example.customer.api.v1.resources.CustomerResource;
import com.example.customer.model.CustomerDetails;
import com.example.customer.persistence.entity.Customer;
import com.example.customer.persistence.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerDetailsServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerDetailsService customerDetailsService;

    @Test
    public void testLoadUserByUserName() {

        // GIVEN
        Customer customer = new Customer().setFirstName("test").setLastName("test").setDateOfBirth(LocalDate.now().minusYears(25));
        when(customerRepository.findByFirstName(anyString())).thenReturn(Optional.of(customer));

        // WHEN
        CustomerDetails res = customerDetailsService.loadUserByUsername("test");

        // THEN
        assertNotNull(res);
        assertEquals(customer.getLastName(), res.getLastName());
        assertEquals(customer.getFirstName(), res.getUsername());
    }

    @Test
    public void testLoadUserByUserNameNotFound() {

        // GIVEN
        when(customerRepository.findByFirstName(anyString())).thenReturn(Optional.empty());

        // THEN
        assertThrows(UsernameNotFoundException.class,
                () -> customerDetailsService.loadUserByUsername("test"));
    }

    @Test
    public void testGet() {

        // GIVEN
        Customer customer = new Customer().setFirstName("test").setLastName("test").setDateOfBirth(LocalDate.now().minusYears(25));
        when(customerRepository.findByFirstName(anyString())).thenReturn(Optional.of(customer));

        // WHEN
        CustomerResource res = customerDetailsService.get("test");

        // THEN
        assertNotNull(res);
        assertEquals(customer.getLastName(), res.getLastName());
        assertEquals("*******", res.getPassword());
    }

    @Test
    public void testGetAllByCriteria() {

        // GIVEN
        Customer c = new Customer().setFirstName("tst").setPassword("test")
                .setDateOfBirth(LocalDate.now().minusYears(25)).setLastName("test");
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(c)));

        // WHEN
        Page<CustomerResource> customerResourcePage = customerDetailsService.getAllByCriteria(PageRequest.of(0, 3));

        // THEN
        assertNotNull(customerResourcePage);
        assertFalse(customerResourcePage.getContent().isEmpty());
    }

    @Test
    public void testUpdate() {

        // GIVEN
        Customer customer = new Customer().setId(1L).setFirstName("test").setLastName("test").setDateOfBirth(LocalDate.now().minusYears(25));
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        Customer updated = new Customer().setId(1L).setFirstName("testupdate").setLastName("test").setDateOfBirth(LocalDate.now().minusYears(25));
        when(customerRepository.save(any(Customer.class))).thenReturn(updated);

        // WHEN
        CustomerResource update = customerDetailsService.update(new CustomerResource().setId(1L).setFirstName("testupdate"));

        // THEN
        assertNotNull(update);
        assertEquals(updated.getFirstName(), update.getFirstName());

    }
}