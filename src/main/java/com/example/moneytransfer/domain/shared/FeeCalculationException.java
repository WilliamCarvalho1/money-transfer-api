package com.example.moneytransfer.domain.shared;

public class FeeCalculationException extends RuntimeException {

    public FeeCalculationException(String message) {
        super(message);
    }
}
