package com.example.moneytransfer.domain.fee;

import com.example.moneytransfer.application.exception.InvalidTransferException;
import com.example.moneytransfer.domain.model.Transfer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class FeeCalculatorService {

    private final List<FeeCalculationStrategy> strategies;

    public FeeCalculatorService(List<FeeCalculationStrategy> strategies) {
        this.strategies = strategies;
    }

    public void applyFeeAndTotalAmount(
            BigDecimal amount,
            LocalDate scheduledDate,
            Transfer transfer
    ) {
        long days = daysUntil(scheduledDate);

        BigDecimal fee = strategies.stream()
                .filter(strategy -> strategy.supports(amount, days))
                .findFirst()
                .map(strategy -> strategy.calculate(amount))
                .orElseThrow(() ->
                        new FeeCalculationException(
                                "No applicable fee rule for amount " + amount + " and days " + days
                        )
                );

        transfer.setFee(fee);
        transfer.setTotalAmount(amount.add(fee));
    }

    private long daysUntil(LocalDate scheduledDate) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), scheduledDate);
        if (days < 0) {
            throw new InvalidTransferException("Scheduled date must not be in the past.");
        }
        return days;
    }
}
