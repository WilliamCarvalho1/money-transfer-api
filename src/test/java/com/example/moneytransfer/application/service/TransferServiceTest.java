package com.example.moneytransfer.application.service;

import com.example.moneytransfer.application.exception.InvalidTransferException;
import com.example.moneytransfer.application.exception.TransferNotFoundException;
import com.example.moneytransfer.application.port.out.TransferRepositoryPort;
import com.example.moneytransfer.domain.model.Transfer;
import com.example.moneytransfer.domain.model.TransferUpdate;
import com.example.moneytransfer.domain.shared.FeeCalculatorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    private final TransferRepositoryPort repository = mock(TransferRepositoryPort.class);
    private final TransferService service = new TransferService(repository, new FeeCalculatorService());

    @Test
    @DisplayName("createTransfer should calculate fee and save transfer")
    void createTransfer_happyPath() {
        LocalDate today = LocalDate.now();
        BigDecimal amount = BigDecimal.valueOf(1000);

        Transfer transfer = new Transfer(
                null,
                "SRC",
                "DEST",
                amount,
                today,
                null,
                null
        );

        when(repository.save(any(Transfer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transfer saved = service.createTransfer(transfer);

        BigDecimal expectedFee = amount.multiply(BigDecimal.valueOf(0.03))
                .add(BigDecimal.valueOf(3));
        BigDecimal expectedTotal = amount.add(expectedFee);

        assertEquals(0, expectedFee.compareTo(saved.getFee()));
        assertEquals(0, expectedTotal.compareTo(saved.getTotalAmount()));
        verify(repository).save(any(Transfer.class));
    }

    @Test
    @DisplayName("getTransfer should throw InvalidTransferException when id is null")
    void getTransfer_nullId_throwsInvalidTransferException() {
        InvalidTransferException ex = assertThrows(InvalidTransferException.class,
                () -> service.getTransfer(null));

        assertEquals("ID must not be null.", ex.getMessage());
    }

    @Test
    @DisplayName("getTransfer should return transfer when found")
    void getTransfer_happyPath() {
        Transfer transfer = new Transfer(
                1L,
                "SRC",
                "DEST",
                BigDecimal.TEN,
                LocalDate.now(),
                BigDecimal.ONE,
                BigDecimal.valueOf(11)
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(transfer));

        Transfer result = service.getTransfer(1L);

        assertSame(transfer, result);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("getTransfer should throw TransferNotFoundException when transfer is not found")
    void getTransfer_notFound_throwsTransferNotFoundException() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(TransferNotFoundException.class, () -> service.getTransfer(1L));
    }

    @Test
    @DisplayName("getTransfer should wrap DataAccessException in InvalidTransferException")
    void getTransfer_databaseError_throwsInvalidTransferException() {
        when(repository.findById(1L))
                .thenThrow(new DataAccessException("DB error") {
                });

        InvalidTransferException ex = assertThrows(InvalidTransferException.class,
                () -> service.getTransfer(1L));

        assertTrue(ex.getMessage().startsWith("Database error: DB error"));
    }

    @Test
    @DisplayName("updateTransfer should throw InvalidTransferException when id is null")
    void updateTransfer_nullId_throwsInvalidTransferException() {
        TransferUpdate update = new TransferUpdate(BigDecimal.TEN, LocalDate.now());

        InvalidTransferException ex = assertThrows(InvalidTransferException.class,
                () -> service.updateTransfer(null, update));

        assertEquals("ID must not be null.", ex.getMessage());
    }

    @Test
    @DisplayName("updateTransfer should recalculate fee and update transfer")
    void updateTransfer_happyPath() {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(5);

        Transfer existing = new Transfer(
                1L,
                "SRC",
                "DEST",
                BigDecimal.TEN,
                today,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        TransferUpdate update = new TransferUpdate(
                BigDecimal.valueOf(1500),
                futureDate
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(existing));
        when(repository.update(any(Transfer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transfer updated = service.updateTransfer(1L, update);

        BigDecimal expectedFee = update.getAmount().multiply(BigDecimal.valueOf(0.09));
        BigDecimal expectedTotal = update.getAmount().add(expectedFee);

        assertEquals(update.getAmount(), updated.getAmount());
        assertEquals(update.getScheduledDate(), updated.getScheduledDate());
        assertEquals(0, expectedFee.compareTo(updated.getFee()));
        assertEquals(0, expectedTotal.compareTo(updated.getTotalAmount()));
        verify(repository).update(any(Transfer.class));
    }

    @Test
    @DisplayName("deleteTransfer should throw InvalidTransferException when id is null")
    void deleteTransfer_nullId_throwsInvalidTransferException() {
        InvalidTransferException ex = assertThrows(InvalidTransferException.class,
                () -> service.deleteTransfer(null));

        assertEquals("ID must not be null.", ex.getMessage());
    }

    @Test
    @DisplayName("deleteTransfer should load transfer and delegate to repository")
    void deleteTransfer_happyPath() {
        Transfer transfer = new Transfer(
                10L,
                "SRC",
                "DEST",
                BigDecimal.TEN,
                LocalDate.now(),
                BigDecimal.ONE,
                BigDecimal.valueOf(11)
        );

        when(repository.findById(10L)).thenReturn(Optional.of(transfer));

        service.deleteTransfer(10L);

        verify(repository).deleteById(10L);
    }
}
