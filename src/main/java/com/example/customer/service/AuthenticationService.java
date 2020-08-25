package com.example.customer.service;

import com.example.customer.api.v1.resources.AuthenticationRequest;
import com.example.customer.api.v1.resources.AuthenticationResponse;
import com.example.customer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getFirstName(), authenticationRequest.getPassword()));

        if (Objects.isNull(authentication) || Objects.isNull(authentication.getPrincipal()))
            throw new AuthenticationServiceException("Authentication Failed");

        String generateToken = JwtUtil.generateToken((UserDetails) authentication.getPrincipal());
        return new AuthenticationResponse().setJwt(generateToken).setExpiresOn(JwtUtil.extractExpiration(generateToken));

    }
}
