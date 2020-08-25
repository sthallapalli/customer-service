package com.example.customer.api.v1.controller;

import com.example.customer.api.v1.resources.AuthenticationRequest;
import com.example.customer.api.v1.resources.AuthenticationResponse;
import com.example.customer.exception.ApiExceptionHandler;
import com.example.customer.service.AuthenticationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    @BeforeAll
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(this.authenticationController)
                .setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    public void testAuthentication() throws Exception {
        // GIVEN
        when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(new AuthenticationResponse()
                        .setJwt("adsdasds.drvdsdsdsds.vfdfdfd")
                        .setExpiresOn(new Date(System.currentTimeMillis() + 1000 * 60 * 60)));

        // WHEN
        mockMvc.perform(post("/api/v1/authenticate")
                .content("{\"firstName\": \"test\",\"password\": \"test\"}")
                .contentType(MediaType.APPLICATION_JSON))

                // THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").exists());
    }

    @Test
    public void testAuthenticationFailure() throws Exception {
        // GIVEN
        when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                .thenThrow(new BadCredentialsException("bad credentials"));

        // WHEN
        mockMvc.perform(post("/api/v1/authenticate")
                .content("{\"firstName\": \"test\",\"password\": \"test\"}")
                .contentType(MediaType.APPLICATION_JSON))

                // THEN
                .andExpect(status().isForbidden());
    }

}
