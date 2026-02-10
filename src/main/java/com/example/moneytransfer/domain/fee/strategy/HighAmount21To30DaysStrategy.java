package com.example.moneytransfer.domain.fee.strategy;

import com.example.moneytransfer.domain.fee.FeeCalculationStrategy;

import java.math.BigDecimal;

public class HighAmount21To30DaysStrategy implements FeeCalculationStrategy {

    @Override
    public boolean supports(BigDecimal amount, long days) {
        return amount.compareTo(BigDecimal.valueOf(2000)) > 0
                && days >= 21 && days <= 30;
    }

    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.069));
    }
}
