package com.example.customer.api.v1.controller;

import com.example.customer.api.v1.resources.AuthenticationRequest;
import com.example.customer.api.v1.resources.AuthenticationResponse;
import com.example.customer.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        log.info("Received an authentication request for customer={}", authenticationRequest.getFirstName());
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

}
