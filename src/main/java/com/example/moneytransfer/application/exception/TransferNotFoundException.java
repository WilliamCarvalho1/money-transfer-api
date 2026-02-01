package com.example.moneytransfer.application.exception;

public class TransferNotFoundException extends RuntimeException {
    public TransferNotFoundException(Long id) {
        super("Transfer not found with id " + id);
    }
}
