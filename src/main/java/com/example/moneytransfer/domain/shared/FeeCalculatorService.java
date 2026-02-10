package com.example.moneytransfer.domain.shared;

import com.example.moneytransfer.application.exception.InvalidTransferException;
import com.example.moneytransfer.domain.model.Transfer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class FeeCalculatorService {

    private static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1000);
    private static final BigDecimal TWO_THOUSAND = BigDecimal.valueOf(2000);

    public void applyFeeAndTotalAmount(BigDecimal amount, LocalDate scheduledDate, Transfer transfer) {
        BigDecimal fee = calculate(amount, scheduledDate);
        transfer.setFee(fee);
        transfer.setTotalAmount(amount.add(fee));
    }

    public BigDecimal calculate(BigDecimal amount, LocalDate scheduledDate) {
        long days = daysUntil(scheduledDate);

        return calculateFee(amount, days)
                .orElseThrow(() ->
                        new FeeCalculationException(
                                "No applicable fee rule for amount " + amount + " and days " + days
                        )
                );
    }

    private long daysUntil(LocalDate scheduledDate) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), scheduledDate);

        if (days < 0) {
            throw new InvalidTransferException("Scheduled date must not be in the past.");
        }
        return days;
    }

    private Optional<BigDecimal> calculateFee(BigDecimal amount, long days) {

        if (isUpToOneThousand(amount) && days == 0) {
            return Optional.of(
                    amount.multiply(BigDecimal.valueOf(0.03))
                            .add(BigDecimal.valueOf(3))
            );
        }

        if (isBetweenOneAndTwoThousand(amount) && isBetweenDays(days, 1, 10)) {
            return Optional.of(amount.multiply(BigDecimal.valueOf(0.09)));
        }

        if (amount.compareTo(TWO_THOUSAND) > 0) {
            return calculateHighAmountFee(amount, days);
        }

        return Optional.empty();
    }

    private Optional<BigDecimal> calculateHighAmountFee(BigDecimal amount, long days) {

        if (isBetweenDays(days, 11, 20)) {
            return Optional.of(amount.multiply(BigDecimal.valueOf(0.082)));
        }
        if (isBetweenDays(days, 21, 30)) {
            return Optional.of(amount.multiply(BigDecimal.valueOf(0.069)));
        }
        if (isBetweenDays(days, 31, 40)) {
            return Optional.of(amount.multiply(BigDecimal.valueOf(0.047)));
        }
        if (days > 40) {
            return Optional.of(amount.multiply(BigDecimal.valueOf(0.017)));
        }

        return Optional.empty();
    }

    private boolean isUpToOneThousand(BigDecimal amount) {
        return amount.compareTo(ONE_THOUSAND) <= 0;
    }

    private boolean isBetweenOneAndTwoThousand(BigDecimal value) {
        return value.compareTo(FeeCalculatorService.ONE_THOUSAND) > 0 &&
                value.compareTo(FeeCalculatorService.TWO_THOUSAND) <= 0;
    }

    private boolean isBetweenDays(long value, long min, long max) {
        return value >= min && value <= max;
    }

}
