# customer-service
This service provides endpoints for authentication based on JWT and for customer profile data criteria based retrieve, update.

### Technology Stack

* Java8
* Spring Boot 2.3.x
* JWT
* Junit5
* Mockito
* Docker
* H2 Embedded DB
* Maven

### How to Run?

Following are the two options to run the server.

1. Run following maven command from project home directory in a terminal 
    
    **mvn spring-boot:run**
    
2. Run in a docker container using following steps

    1. Build the docker image using following command from project home directory in a terminal
    
        **docker build --tag customer-service:1.0 .**
        
    2. Run an instance of the docker image with following command and the service will be available on port 8000. 
    
         **docker run --publish 9000:8080  --name customerservice customer-service:1.0**
         
## Prerequisite Data

- On server startup the following five customer from **$PROJECT_HOME/resources/schema.sql** will be available in DB

### How to Test?

Following are the two options to test the service

### Postman
* Please import the postman collection from project to perform operations. 
* Please use the latest jwt token from authentication endpoint for calling customer profile apis.

### Curl
**Requires JQ json parsing tool installation**

- Run **$PROJECT_HOME/bin/customer-service.sh** script from project home. It will display all operations.


### Test results
- Please check **postman-results.docx** file from project home for sample test results
