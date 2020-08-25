package com.example.customer.api.v1.controller;

import com.example.customer.api.v1.resources.CustomerResource;
import com.example.customer.exception.ApiExceptionHandler;
import com.example.customer.service.CustomerDetailsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerControllerTest {

    @Mock
    private CustomerDetailsService customerDetailsService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeAll
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(this.customerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    public void testSearch() throws Exception {

        // GIVEN
        CustomerResource cr = new CustomerResource().setFirstName("tst").setPassword("test")
                .setDateOfBirth(LocalDate.now().minusYears(25)).setLastName("test");
        when(customerDetailsService.getAllByCriteria(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(cr)));

        // WHEN
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customer/search?page=0"))

                // THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    public void testGetByName() throws Exception {
        // GIVEN
        CustomerResource cr = new CustomerResource().setFirstName("test").setPassword("test")
                .setDateOfBirth(LocalDate.now().minusYears(25)).setLastName("test");
        when(customerDetailsService.get(anyString())).thenReturn(cr);

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customer/test"))

                // THEN
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", is("test")));
    }

    @Test
    public void testGetByNameFail() throws Exception {
        // GIVEN
        when(customerDetailsService.get(anyString())).thenThrow(new UsernameNotFoundException("user not found"));

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customer/test"))

                // THEN
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdate() throws Exception {
        // GIVEN
        CustomerResource cr = new CustomerResource().setFirstName("test").setPassword("test")
                .setDateOfBirth(LocalDate.now().minusYears(25)).setLastName("testupdate");
        when(customerDetailsService.update(any(CustomerResource.class))).thenReturn(cr);

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customer")
                .content("{\"id\":1,\"firstName\": \"test\",\"lastName\": \"testupdate\"}")
                .contentType(MediaType.APPLICATION_JSON))

                // THEN
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", is("testupdate")));
    }

    @Test
    public void testUpdateFail() throws Exception {
        // GIVEN
        CustomerResource cr = new CustomerResource().setFirstName("test").setPassword("test")
                .setDateOfBirth(LocalDate.now().minusYears(25)).setLastName("testupdate");
        when(customerDetailsService.update(any(CustomerResource.class))).thenReturn(cr);

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customer")
                .content("{\"lastName\": \"testupdate\"}")
                .contentType(MediaType.APPLICATION_JSON))

                // THEN
                .andExpect(status().isBadRequest());
    }
}
