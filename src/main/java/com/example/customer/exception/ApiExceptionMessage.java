package com.example.customer.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Setter
@Getter
public class ApiExceptionMessage {

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    public ApiExceptionMessage(HttpStatus httpStatus, String message, Throwable th) {
        this.status = httpStatus;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
