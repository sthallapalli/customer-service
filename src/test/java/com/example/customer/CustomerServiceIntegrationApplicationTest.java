package com.example.customer;

import com.example.customer.api.v1.resources.AuthenticationRequest;
import com.example.customer.api.v1.resources.AuthenticationResponse;
import com.example.customer.api.v1.resources.CustomerResource;
import com.example.customer.helper.CustomPageImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerServiceIntegrationApplicationTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Test
    public void testAuthentication() {

        AuthenticationRequest request = new AuthenticationRequest().setFirstName("test").setPassword("test");
        ResponseEntity<AuthenticationResponse> responseEntity = getAuthenticationResponse(request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getJwt()).isNotEmpty();
        assertThat(responseEntity.getBody().getExpiresOn()).isAfter(new Date());
    }

    @Test
    public void testFailAuthentication() {

        AuthenticationRequest request = new AuthenticationRequest().setFirstName("test").setPassword("testwrong");
        ResponseEntity<AuthenticationResponse> responseEntity = getAuthenticationResponse(request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void testCustomerByFirstName() {
        AuthenticationRequest request = new AuthenticationRequest().setFirstName("test").setPassword("test");

        HttpHeaders headers = getJwtAuthorizationHeaders(request);
        ResponseEntity<CustomerResource> responseEntity = testRestTemplate.exchange(createURLWithPort("/api/v1/customer/test"),
                HttpMethod.GET, new HttpEntity<>(null, headers), CustomerResource.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getFirstName()).isEqualTo("test");
    }

    @Test
    public void testCustomerByFirstNameNoAuthorization() {
        ResponseEntity<CustomerResource> responseEntity = testRestTemplate.exchange(createURLWithPort("/api/v1/customer/test"),
                HttpMethod.GET, new HttpEntity<>(null, new HttpHeaders()), CustomerResource.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void testCustomerByFirstNameNoCustomerFound() {

        AuthenticationRequest request = new AuthenticationRequest().setFirstName("test").setPassword("test");
        HttpHeaders headers = getJwtAuthorizationHeaders(request);

        ResponseEntity<CustomerResource> responseEntity = testRestTemplate.exchange(
                createURLWithPort("/api/v1/customer/tetwronguser"),
                HttpMethod.GET, new HttpEntity<>(null, headers), CustomerResource.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testSearchFirstThreeCustomersOfFiveInDoBDesc() {
        AuthenticationRequest request = new AuthenticationRequest().setFirstName("test").setPassword("test");
        HttpHeaders headers = getJwtAuthorizationHeaders(request);

        ResponseEntity<CustomPageImpl> responseEntity = testRestTemplate.exchange(
                createURLWithPort("/api/v1/customer/search?sort=dateOfBirth,desc&page=0&size=3"),
                HttpMethod.GET, new HttpEntity<>(null, headers), CustomPageImpl.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Proof of fetching only 3 customers of total 5 customers in db
        assertThat(responseEntity.getBody().getNumberOfElements()).isEqualTo(3);
        assertThat(responseEntity.getBody().getTotalElements()).isEqualTo(5);
        assertThat(responseEntity.getBody().getTotalPages()).isEqualTo(2);

        // Proof for list of customer order by dateOfBirth desc
        List<CustomerResource> customerResources = responseEntity.getBody().getContent();
        assertThat(customerResources.get(0).getDateOfBirth()).isAfter(customerResources.get(1).getDateOfBirth());
        assertThat(customerResources.get(1).getDateOfBirth()).isAfter(customerResources.get(2).getDateOfBirth());
    }

    @Test
    public void testUpdate() {
        AuthenticationRequest request = new AuthenticationRequest().setFirstName("test").setPassword("test");
        HttpHeaders headers = getJwtAuthorizationHeaders(request);

        CustomerResource customerResource = new CustomerResource().setId(1L)
                .setFirstName("test").setLastName("testupdate")
                .setPassword("test");
        HttpEntity<CustomerResource> entity = new HttpEntity<>(customerResource, headers);

        ResponseEntity<CustomerResource> responseEntity = testRestTemplate.exchange(
                createURLWithPort("/api/v1/customer"),
                HttpMethod.PUT, entity, CustomerResource.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getLastName()).isEqualTo(customerResource.getLastName());
    }

    private HttpHeaders getJwtAuthorizationHeaders(AuthenticationRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthenticationResponse(request).getBody().getJwt());
        return headers;
    }

    private ResponseEntity<AuthenticationResponse> getAuthenticationResponse(AuthenticationRequest request) {
        HttpEntity<AuthenticationRequest> entity = new HttpEntity<>(request, new HttpHeaders());

        return testRestTemplate.exchange(createURLWithPort("/api/v1/authenticate"),
                HttpMethod.POST, entity, AuthenticationResponse.class);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
