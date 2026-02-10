package com.example.moneytransfer.domain.fee;

import com.example.moneytransfer.application.exception.InvalidTransferException;
import com.example.moneytransfer.domain.model.Transfer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FeeCalculatorServiceTest {

    private final FeeCalculationStrategy strategy = mock(FeeCalculationStrategy.class);
    private final FeeCalculatorService feeCalculatorService =
            new FeeCalculatorService(List.of(strategy));

    @ParameterizedTest
    @MethodSource("feeCases")
    @DisplayName("applyFeeAndTotalAmount should apply expected fee for all scenarios")
    void applyFeeAndTotalAmount_should_apply_expected_fee(
            BigDecimal amount,
            long daysToAdd,
            BigDecimal expectedFee
    ) {
        LocalDate scheduledDate = LocalDate.now().plusDays(daysToAdd);
        Transfer transfer = new Transfer();

        long days = daysToAdd;

        when(strategy.supports(amount, days)).thenReturn(true);
        when(strategy.calculate(amount)).thenReturn(expectedFee);

        feeCalculatorService.applyFeeAndTotalAmount(amount, scheduledDate, transfer);

        assertEquals(0, expectedFee.compareTo(transfer.getFee()));
        assertEquals(0, amount.add(expectedFee).compareTo(transfer.getTotalAmount()));

        verify(strategy).supports(amount, days);
        verify(strategy).calculate(amount);
    }

    private static Stream<Arguments> feeCases() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(1000), 0L, BigDecimal.valueOf(33)),   // example: 3% + 3 = 33
                Arguments.of(BigDecimal.valueOf(1500), 5L, BigDecimal.valueOf(135)),  // example: 9% of 1500
                Arguments.of(BigDecimal.valueOf(3000), 15L, BigDecimal.valueOf(246))  // example: 8.2% of 3000
        );
    }

    @Test
    @DisplayName("applyFeeAndTotalAmount should throw InvalidTransferException when scheduled date is in the past")
    void applyFeeAndTotalAmount_should_throw_when_date_in_past() {
        BigDecimal amount = BigDecimal.TEN;
        LocalDate pastDate = LocalDate.now().minusDays(1);
        Transfer transfer = new Transfer();

        InvalidTransferException ex = assertThrows(
                InvalidTransferException.class,
                () -> feeCalculatorService.applyFeeAndTotalAmount(amount, pastDate, transfer)
        );

        assertEquals("Scheduled date must not be in the past.", ex.getMessage());
        verifyNoInteractions(strategy);
    }

    @Test
    @DisplayName("applyFeeAndTotalAmount should throw FeeCalculationException when no fee rule applies")
    void applyFeeAndTotalAmount_should_throw_when_no_applicable_rule() {
        BigDecimal amount = BigDecimal.valueOf(500);
        LocalDate scheduledDate = LocalDate.now().plusDays(1);
        long days = 1L;
        Transfer transfer = new Transfer();

        when(strategy.supports(amount, days)).thenReturn(false);

        FeeCalculationException ex = assertThrows(
                FeeCalculationException.class,
                () -> feeCalculatorService.applyFeeAndTotalAmount(amount, scheduledDate, transfer)
        );

        assertTrue(ex.getMessage().startsWith("No applicable fee rule for amount"));
        verify(strategy).supports(amount, days);
        verify(strategy, never()).calculate(any());
    }
}
