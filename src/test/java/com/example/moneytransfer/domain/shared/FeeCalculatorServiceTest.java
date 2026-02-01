package com.example.moneytransfer.domain.shared;

import com.example.moneytransfer.domain.model.Transfer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeeCalculatorServiceTest {

    private final FeeCalculatorService feeCalculatorService = new FeeCalculatorService();

    @ParameterizedTest
    @MethodSource("feeCases")
    @DisplayName("calculate should apply expected fee for all scenarios")
    void calculate_should_apply_expected_fee(BigDecimal amount, long daysToAdd, BigDecimal rate, BigDecimal fixedPart) {
        LocalDate scheduledDate = LocalDate.now().plusDays(daysToAdd);

        BigDecimal fee = feeCalculatorService.calculate(amount, scheduledDate);
        BigDecimal expected = amount.multiply(rate).add(fixedPart);

        assertEquals(0, expected.compareTo(fee));
    }

    private static Stream<Arguments> feeCases() {
        return Stream.of(
                // up to 1000, same day: 3% + 3
                Arguments.of(BigDecimal.valueOf(1000), 0L, BigDecimal.valueOf(0.03), BigDecimal.valueOf(3)),
                // 1000–2000, 1–10 days: 9%
                Arguments.of(BigDecimal.valueOf(1500), 5L, BigDecimal.valueOf(0.09), BigDecimal.ZERO),
                // >2000, 11–20 days: 8.2%
                Arguments.of(BigDecimal.valueOf(3000), 15L, BigDecimal.valueOf(0.082), BigDecimal.ZERO),
                // >2000, 21–30 days: 6.9%
                Arguments.of(BigDecimal.valueOf(3000), 25L, BigDecimal.valueOf(0.069), BigDecimal.ZERO),
                // >2000, 31–40 days: 4.7%
                Arguments.of(BigDecimal.valueOf(3000), 35L, BigDecimal.valueOf(0.047), BigDecimal.ZERO),
                // >2000, >40 days: 1.7%
                Arguments.of(BigDecimal.valueOf(3000), 50L, BigDecimal.valueOf(0.017), BigDecimal.ZERO)
        );
    }

    @Test
    @DisplayName("calculate should throw IllegalArgumentException when scheduled date is in the past")
    void calculate_should_throw_when_date_in_past() {
        BigDecimal amount = BigDecimal.TEN;
        LocalDate pastDate = LocalDate.now().minusDays(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> feeCalculatorService.calculate(amount, pastDate));

        assertEquals("Scheduled date must not be in the past.", ex.getMessage());
    }

    @Test
    @DisplayName("calculate should throw InvalidTransferException when no fee rule applies")
    void calculate_should_throw_when_no_applicable_rule() {
        BigDecimal amount = BigDecimal.valueOf(500); // up to 1000 but not same day
        LocalDate scheduledDate = LocalDate.now().plusDays(1); // days > 0

        FeeCalculationException ex = assertThrows(FeeCalculationException.class,
                () -> feeCalculatorService.calculate(amount, scheduledDate));

        assertTrue(ex.getMessage().startsWith("No applicable fee rule for amount"));
    }

    @Test
    @DisplayName("setFeeAndTotalAmount should populate fee and totalAmount on transfer")
    void setFeeAndTotalAmount_shouldPopulateTransfer() {
        BigDecimal amount = BigDecimal.valueOf(1000);
        LocalDate today = LocalDate.now();
        Transfer transfer = new Transfer();

        feeCalculatorService.setFeeAndTotalAmount(amount, today, transfer);

        BigDecimal expectedFee = amount.multiply(BigDecimal.valueOf(0.03))
                .add(BigDecimal.valueOf(3));
        BigDecimal expectedTotal = amount.add(expectedFee);

        assertEquals(0, expectedFee.compareTo(transfer.getFee()));
        assertEquals(0, expectedTotal.compareTo(transfer.getTotalAmount()));
    }
}
