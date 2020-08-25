package com.example.customer.api.v1.resources;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class AuthenticationRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String password;
}
