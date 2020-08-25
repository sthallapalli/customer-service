package com.example.customer.api.v1.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResource {

    @NotNull
    private Long id;

    @NotNull
    private String firstName;
    private String lastName;
    private String password;
    private LocalDate dateOfBirth;
}
