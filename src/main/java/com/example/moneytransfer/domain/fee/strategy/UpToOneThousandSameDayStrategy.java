package com.example.moneytransfer.domain.fee.strategy;

import com.example.moneytransfer.domain.fee.FeeCalculationStrategy;

import java.math.BigDecimal;

public class UpToOneThousandSameDayStrategy implements FeeCalculationStrategy {

    @Override
    public boolean supports(BigDecimal amount, long days) {
        return amount.compareTo(BigDecimal.valueOf(1000)) <= 0 && days == 0;
    }

    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.03))
                .add(BigDecimal.valueOf(3));
    }
}
