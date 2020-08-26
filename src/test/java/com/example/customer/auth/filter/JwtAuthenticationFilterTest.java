package com.example.customer.auth.filter;

import com.example.customer.model.CustomerDetails;
import com.example.customer.persistence.entity.Customer;
import com.example.customer.service.CustomerDetailsService;
import com.example.customer.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtAuthenticationFilterTest {

    @Mock
    private CustomerDetailsService customerDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Test
    public void testASuccess() throws Exception {

        when(customerDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new CustomerDetails(new Customer().setFirstName("test")
                        .setLastName("test").setPassword("test")));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + JwtUtil.generateToken(new CustomerDetails(new Customer().setFirstName("test").setLastName("test").setPassword("test"))));
        request.setServletPath("/api/v1/customers/test");

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        FilterChain chain = mock(FilterChain.class);

        jwtAuthenticationFilter.doFilter(request, new MockHttpServletResponse(), chain);

        verify(chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo("test");
        assertThat(SecurityContextHolder.getContext().getAuthentication().getCredentials()).isEqualTo("test");
    }


    @Test
    public void testFailNoAuthorizationHeader() throws Exception {

        when(customerDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new CustomerDetails(new Customer().setFirstName("test")
                        .setLastName("test").setPassword("test")));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/v1/customer/test");

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        FilterChain chain = mock(FilterChain.class);

        jwtAuthenticationFilter.doFilter(request, new MockHttpServletResponse(), chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

    }

    @Test
    public void testFailNoUserFound() throws Exception {

        when(customerDetailsService.loadUserByUsername("test"))
                .thenThrow(new UsernameNotFoundException("User not found"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/v1/customer/test");

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        FilterChain chain = mock(FilterChain.class);

        jwtAuthenticationFilter.doFilter(request, new MockHttpServletResponse(), chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }


}
