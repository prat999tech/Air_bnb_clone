package com.example.airbnb.exception;

public class Unauthorizedexception extends RuntimeException {
    public Unauthorizedexception(String message) {
        super(message);
    }
}
