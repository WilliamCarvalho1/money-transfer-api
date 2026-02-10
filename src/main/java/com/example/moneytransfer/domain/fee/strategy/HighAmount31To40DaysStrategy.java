package com.example.moneytransfer.domain.fee.strategy;

import com.example.moneytransfer.domain.fee.FeeCalculationStrategy;

import java.math.BigDecimal;

public class HighAmount31To40DaysStrategy implements FeeCalculationStrategy {

    @Override
    public boolean supports(BigDecimal amount, long days) {
        return amount.compareTo(BigDecimal.valueOf(2000)) > 0
                && days >= 31 && days <= 40;
    }

    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.047));
    }
}