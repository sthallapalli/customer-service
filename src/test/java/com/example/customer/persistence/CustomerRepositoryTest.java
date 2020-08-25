package com.example.customer.persistence;

import com.example.customer.persistence.entity.Customer;
import com.example.customer.persistence.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;


    @Test
    public void testSave() {
        // GIVEN
        Customer customer = new Customer().setFirstName("tin").setLastName("ton")
                .setPassword("tinton").setDateOfBirth(LocalDate.now().minusYears(25));

        // WHEN
        Customer savedCustomer = customerRepository.save(customer);

        // THEN
        assertNotNull(savedCustomer);
        assertNotNull(savedCustomer.getId());
        assertNotNull(savedCustomer.getAudit().getCreatedOn());
    }
}
