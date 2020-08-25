package com.example.customer.service;

import com.example.customer.api.v1.resources.CustomerResource;
import com.example.customer.model.CustomerDetails;
import com.example.customer.persistence.entity.Customer;
import com.example.customer.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Customer> user = customerRepository.findByFirstName(s);
        user.orElseThrow(() -> new UsernameNotFoundException("Customer not found: " + s));

        return user.map(CustomerDetails::new).get();
    }

    @Transactional
    public CustomerResource get(String firstName) {
        Customer customer = customerRepository.findByFirstName(firstName)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found: " + firstName));

        return toResource(customer);
    }

    @Transactional
    public Page<CustomerResource> getAllByCriteria(Pageable pageable) {
        Page<Customer> all = customerRepository.findAll(pageable);
        return all.map(this::toResource);
    }

    @Transactional
    public CustomerResource update(CustomerResource customerResource) {
        Customer customer = customerRepository.findById(customerResource.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Customer id not found: " + customerResource.getId()));

        Customer result = customerRepository.save(toEntity(customerResource, customer));
        return toResource(result);
    }


    private CustomerResource toResource(Customer customer) {
        CustomerResource cr = new CustomerResource();
        BeanUtils.copyProperties(customer, cr);
        cr.setPassword("*******");
        return cr;
    }

    // For simplicity, used this approach and complex mappings mapstruct is more cleaner
    private Customer toEntity(CustomerResource customerResource, Customer existingCustomer) {

        if (Objects.nonNull(customerResource.getFirstName()))
            existingCustomer.setFirstName(customerResource.getFirstName());

        if (Objects.nonNull(customerResource.getLastName()))
            existingCustomer.setLastName(customerResource.getLastName());

        if (Objects.nonNull(customerResource.getDateOfBirth()))
            existingCustomer.setDateOfBirth(customerResource.getDateOfBirth());

        if (Objects.nonNull(customerResource.getPassword()))
            existingCustomer.setPassword(customerResource.getPassword());

        return existingCustomer;
    }

}