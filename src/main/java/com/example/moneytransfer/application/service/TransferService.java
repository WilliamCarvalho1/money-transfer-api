package com.example.moneytransfer.application.service;

import com.example.moneytransfer.application.exception.InvalidTransferException;
import com.example.moneytransfer.application.exception.TransferNotFoundException;
import com.example.moneytransfer.application.port.in.CreateTransferUseCase;
import com.example.moneytransfer.application.port.in.DeleteTransferUseCase;
import com.example.moneytransfer.application.port.in.GetTransferUseCase;
import com.example.moneytransfer.application.port.in.UpdateTransferUseCase;
import com.example.moneytransfer.application.port.out.TransferRepositoryPort;
import com.example.moneytransfer.domain.model.Transfer;
import com.example.moneytransfer.domain.model.TransferUpdate;
import com.example.moneytransfer.domain.shared.FeeCalculationException;
import com.example.moneytransfer.domain.shared.FeeCalculatorService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransferService implements CreateTransferUseCase, GetTransferUseCase, UpdateTransferUseCase, DeleteTransferUseCase {

    private final TransferRepositoryPort repository;
    private final FeeCalculatorService feeCalculator;

    public TransferService(TransferRepositoryPort repository, FeeCalculatorService feeCalculator) {
        this.repository = repository;
        this.feeCalculator = feeCalculator;
    }

    @Override
    public Transfer createTransfer(Transfer request) {
        try {
            feeCalculator.setFeeAndTotalAmount(request.getAmount(), request.getScheduledDate(), request);
        } catch (FeeCalculationException ex) {
            throw new InvalidTransferException(ex.getMessage());
        }

        return repository.save(request);
    }

    @Override
    public Transfer getTransfer(Long id) {
        try {
            Optional<Transfer> retrievedTransfer = repository.findById(id);
            if (retrievedTransfer.isPresent()) {
                return retrievedTransfer.get();
            }
        } catch (DataAccessException ex) {
            throw new InvalidTransferException("Database error: " + ex.getMessage());
        }
        throw new TransferNotFoundException(id);
    }

    @Override
    public Transfer updateTransfer(Long id, TransferUpdate request) {
        Transfer retrievedTransfer = getTransfer(id);

        try {
            feeCalculator.setFeeAndTotalAmount(request.getAmount(), request.getScheduledDate(), retrievedTransfer);
        } catch (FeeCalculationException ex) {
            throw new InvalidTransferException(ex.getMessage());
        }

        retrievedTransfer.setAmount(request.getAmount());
        retrievedTransfer.setScheduledDate(request.getScheduledDate());

        return repository.update(retrievedTransfer);
    }

    @Override
    public void deleteTransfer(Long id) {
        Transfer retrievedTransfer = getTransfer(id);
        repository.deleteById(retrievedTransfer.getId());
    }

}
