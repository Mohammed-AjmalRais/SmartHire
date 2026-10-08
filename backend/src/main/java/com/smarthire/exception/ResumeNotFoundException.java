package com.smarthire.exception;

public class ResumeNotFoundException extends ResourceNotFoundException {
    public ResumeNotFoundException(String message) {
        super(message);
    }
}
