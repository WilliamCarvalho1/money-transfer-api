package com.example.moneytransfer.application.port.in;

import com.example.moneytransfer.domain.model.Transfer;

public interface GetTransferUseCase {
    Transfer getTransfer(Long id);
}
