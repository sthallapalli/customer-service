package com.example.customer.api.v1.resources;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class AuthenticationResponse {

    private String jwt;
    private Date expiresOn;

}
