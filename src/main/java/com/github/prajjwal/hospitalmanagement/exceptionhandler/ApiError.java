package com.github.prajjwal.hospitalmanagement.exceptionhandler;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Data
public class ApiError {
    private LocalDate timestamp;
    private String message;
    private HttpStatus statusCode;

    private ApiError() {
        this.timestamp = LocalDate.now();
    }
    public ApiError(String error, HttpStatus status) {
        this();
        this.statusCode = status;
        this.message = error;
    }
}