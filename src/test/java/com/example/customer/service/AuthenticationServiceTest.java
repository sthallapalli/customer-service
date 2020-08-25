package com.example.customer.service;

import com.example.customer.api.v1.resources.AuthenticationRequest;
import com.example.customer.api.v1.resources.AuthenticationResponse;
import com.example.customer.model.CustomerDetails;
import com.example.customer.persistence.entity.Customer;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void testAuthenticate() {
        // GIVEN
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                new CustomerDetails(new Customer().setFirstName("test").setLastName("test")), null, Lists.emptyList());
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(usernamePasswordAuthenticationToken);

        // WHEN
        AuthenticationResponse authenticate = authenticationService.authenticate(new AuthenticationRequest().setFirstName("test").setPassword("test"));

        // THEN
        assertNotNull(authenticate);
        assertNotNull(authenticate.getJwt());
        assertNotNull(authenticate.getExpiresOn());
    }

    @Test
    public void testFailAuthenticate() {
        // GIVEN
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, Lists.emptyList());
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(usernamePasswordAuthenticationToken);

        // THEN
        assertThrows(AuthenticationServiceException.class,
                () -> authenticationService.authenticate(new AuthenticationRequest().setFirstName("test").setPassword("test")));
    }

}
