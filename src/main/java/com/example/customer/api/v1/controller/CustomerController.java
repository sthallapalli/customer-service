package com.example.customer.api.v1.controller;

import com.example.customer.api.v1.resources.CustomerResource;
import com.example.customer.service.CustomerDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerDetailsService customerDetailsService;

    @GetMapping("/search")
    public ResponseEntity<Page<CustomerResource>> getAll(Pageable pageable) {
        return ResponseEntity.ok(customerDetailsService.getAllByCriteria(pageable));
    }

    @GetMapping(value = "/{first-name}")
    public ResponseEntity<CustomerResource> get(@PathVariable(name = "first-name") @NotNull String firstName) {
        log.info("Request received to retrieve customer={}", firstName);
        return ResponseEntity.ok(customerDetailsService.get(firstName));
    }

    @PutMapping
    public ResponseEntity<CustomerResource> update(@RequestBody @Valid CustomerResource customerResource) {
        log.info("Request received to update customer={}", customerResource.getFirstName());
        CustomerResource resource = customerDetailsService.update(customerResource);
        return ResponseEntity.ok(resource);
    }
}
