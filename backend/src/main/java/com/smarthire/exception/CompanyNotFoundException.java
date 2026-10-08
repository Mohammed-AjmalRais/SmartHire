package com.smarthire.exception;

public class CompanyNotFoundException extends ResourceNotFoundException {
    public CompanyNotFoundException(String message) {
        super(message);
    }
}
