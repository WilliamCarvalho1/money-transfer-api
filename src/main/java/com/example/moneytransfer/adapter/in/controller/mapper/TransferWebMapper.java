package com.example.moneytransfer.adapter.in.controller.mapper;

import com.example.moneytransfer.adapter.in.controller.dto.TransferRequestDTO;
import com.example.moneytransfer.adapter.in.controller.dto.TransferResponseDTO;
import com.example.moneytransfer.domain.model.Transfer;

public class TransferWebMapper {

    private TransferWebMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Transfer toDomain(TransferRequestDTO requestDTO) {

        return new Transfer(
                null,
                requestDTO.sourceAccount(),
                requestDTO.destinationAccount(),
                requestDTO.amount(),
                requestDTO.scheduledDate(),
                null,
                null
        );
    }

    public static TransferResponseDTO toResponseDTO(Transfer transfer) {

        return new TransferResponseDTO(
                transfer.getId(),
                transfer.getSourceAccount(),
                transfer.getDestinationAccount(),
                transfer.getAmount(),
                transfer.getScheduledDate(),
                transfer.getFee(),
                transfer.getTotalAmount()
        );

    }
}
