package com.example.moneytransfer.application.port.in;

import com.example.moneytransfer.domain.model.Transfer;
import com.example.moneytransfer.domain.model.TransferUpdate;

public interface UpdateTransferUseCase {
    Transfer updateTransfer(Long id, TransferUpdate request);
}
