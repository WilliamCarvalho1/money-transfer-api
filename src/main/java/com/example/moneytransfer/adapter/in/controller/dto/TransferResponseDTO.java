package com.example.moneytransfer.adapter.in.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferResponseDTO(
        Long id,
        String sourceAccount,
        String destinationAccount,
        BigDecimal amount,
        LocalDate scheduledDate,
        BigDecimal fee,
        BigDecimal totalAmount
) {
}
