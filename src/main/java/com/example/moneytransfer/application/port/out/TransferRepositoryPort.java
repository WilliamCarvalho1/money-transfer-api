package com.example.moneytransfer.application.port.out;

import com.example.moneytransfer.domain.model.Transfer;

import java.util.Optional;

public interface TransferRepositoryPort {
    Transfer save(Transfer transfer);

    Optional<Transfer> findById(Long id);

    Transfer update(Transfer updatedTransfer);

    void deleteById(Long id);
}