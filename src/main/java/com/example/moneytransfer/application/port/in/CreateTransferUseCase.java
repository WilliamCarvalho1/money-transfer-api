package com.example.moneytransfer.application.port.in;

import com.example.moneytransfer.domain.model.Transfer;

public interface CreateTransferUseCase {
    Transfer createTransfer(Transfer request);
}
