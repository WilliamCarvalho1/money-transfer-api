package com.example.moneytransfer.domain.fee;

import java.math.BigDecimal;

public interface FeeCalculationStrategy {

    boolean supports(BigDecimal amount, long daysUntilTransfer);

    BigDecimal calculate(BigDecimal amount);
}