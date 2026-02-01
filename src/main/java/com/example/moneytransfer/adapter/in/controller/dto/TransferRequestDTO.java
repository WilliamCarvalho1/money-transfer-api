package com.example.moneytransfer.adapter.in.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferRequestDTO(
        @NotNull @NotBlank String sourceAccount,
        @NotNull @NotBlank String destinationAccount,
        @NotNull @Positive BigDecimal amount,
        @NotNull LocalDate scheduledDate
) {
}
