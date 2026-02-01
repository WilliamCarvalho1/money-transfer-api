package com.example.moneytransfer.adapter.in.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferUpdateRequestDTO(
        @NotNull @Positive BigDecimal amount,
        @NotNull LocalDate scheduledDate
) {
}
