package com.example.moneytransfer.adapter.in.controller.mapper;

import com.example.moneytransfer.adapter.in.controller.dto.TransferUpdateRequestDTO;
import com.example.moneytransfer.domain.model.TransferUpdate;

public class TransferUpdateWebMapper {

    private TransferUpdateWebMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static TransferUpdate toDomain(TransferUpdateRequestDTO requestDTO) {

        return new TransferUpdate(
                requestDTO.amount(),
                requestDTO.scheduledDate()
        );
    }
}
