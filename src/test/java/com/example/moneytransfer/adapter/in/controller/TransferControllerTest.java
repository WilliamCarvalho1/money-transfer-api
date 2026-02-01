package com.example.moneytransfer.adapter.in.controller;

import com.example.moneytransfer.adapter.in.controller.dto.TransferRequestDTO;
import com.example.moneytransfer.adapter.in.controller.dto.TransferUpdateRequestDTO;
import com.example.moneytransfer.adapter.in.controller.error.ErrorCode;
import com.example.moneytransfer.application.port.in.CreateTransferUseCase;
import com.example.moneytransfer.application.port.in.DeleteTransferUseCase;
import com.example.moneytransfer.application.port.in.GetTransferUseCase;
import com.example.moneytransfer.application.port.in.UpdateTransferUseCase;
import com.example.moneytransfer.domain.model.Transfer;
import com.example.moneytransfer.domain.model.TransferUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransferController.class)
@Import(GlobalExceptionHandler.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateTransferUseCase createTransferUseCase;

    @MockBean
    private GetTransferUseCase getTransferUseCase;

    @MockBean
    private UpdateTransferUseCase updateTransferUseCase;

    @MockBean
    private DeleteTransferUseCase deleteTransferUseCase;

    @Test
    @DisplayName("createTransfer should return created transfer")
    void createTransfer_shouldReturnCreatedTransfer() throws Exception {
        LocalDate date = LocalDate.of(2026, 2, 1);

        Transfer createdTransfer = new Transfer(
                1L,
                "acc1",
                "acc2",
                BigDecimal.TEN,
                date,
                BigDecimal.ONE,
                BigDecimal.valueOf(11)
        );

        when(createTransferUseCase.createTransfer(any(Transfer.class)))
                .thenReturn(createdTransfer);

        TransferRequestDTO requestDTO = new TransferRequestDTO(
                "acc1",
                "acc2",
                BigDecimal.TEN,
                date
        );

        mockMvc.perform(post("/api/v1/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.sourceAccount").value("acc1"))
                .andExpect(jsonPath("$.destinationAccount").value("acc2"))
                .andExpect(jsonPath("$.amount").value(10))
                .andExpect(jsonPath("$.scheduledDate").value(date.toString()))
                .andExpect(jsonPath("$.fee").value(1))
                .andExpect(jsonPath("$.totalAmount").value(11));

        verify(createTransferUseCase).createTransfer(any(Transfer.class));
    }

    @Test
    @DisplayName("getTransfer should return existing transfer")
    void getTransfer_shouldReturnExistingTransfer() throws Exception {
        LocalDate date = LocalDate.of(2026, 2, 2);

        Transfer transfer = new Transfer(
                2L,
                "SRC",
                "DEST",
                BigDecimal.valueOf(50),
                date,
                BigDecimal.valueOf(2),
                BigDecimal.valueOf(52)
        );

        when(getTransferUseCase.getTransfer(2L)).thenReturn(transfer);

        mockMvc.perform(get("/api/v1/transfer/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.sourceAccount").value("SRC"))
                .andExpect(jsonPath("$.destinationAccount").value("DEST"))
                .andExpect(jsonPath("$.amount").value(50))
                .andExpect(jsonPath("$.scheduledDate").value(date.toString()))
                .andExpect(jsonPath("$.fee").value(2))
                .andExpect(jsonPath("$.totalAmount").value(52));

        verify(getTransferUseCase).getTransfer(2L);
    }

    @Test
    @DisplayName("updateTransfer should return updated transfer")
    void updateTransfer_shouldReturnUpdatedTransfer() throws Exception {
        LocalDate updatedDate = LocalDate.of(2026, 2, 10);

        Transfer updatedTransfer = new Transfer(
                3L,
                "SRC3",
                "DEST3",
                BigDecimal.valueOf(200),
                updatedDate,
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(205)
        );

        when(updateTransferUseCase.updateTransfer(eq(3L), any(TransferUpdate.class)))
                .thenReturn(updatedTransfer);

        TransferUpdateRequestDTO updateRequest = new TransferUpdateRequestDTO(
                BigDecimal.valueOf(200),
                updatedDate
        );

        mockMvc.perform(put("/api/v1/transfer/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.sourceAccount").value("SRC3"))
                .andExpect(jsonPath("$.destinationAccount").value("DEST3"))
                .andExpect(jsonPath("$.amount").value(200))
                .andExpect(jsonPath("$.scheduledDate").value(updatedDate.toString()))
                .andExpect(jsonPath("$.fee").value(5))
                .andExpect(jsonPath("$.totalAmount").value(205));

        verify(updateTransferUseCase).updateTransfer(eq(3L), any(TransferUpdate.class));
    }

    @Test
    @DisplayName("deleteTransfer should invoke use case and return OK")
    void deleteTransfer_shouldInvokeUseCaseAndReturnOk() throws Exception {
        Long id = 4L;

        mockMvc.perform(delete("/api/v1/transfer/{id}", id))
                .andExpect(status().isOk());

        verify(deleteTransferUseCase).deleteTransfer(id);
    }

    @Test
    @DisplayName("createTransfer should return validation error for invalid request")
    void createTransfer_shouldReturnValidationError() throws Exception {
        LocalDate date = LocalDate.of(2026, 2, 1);

        TransferRequestDTO invalidRequest = new TransferRequestDTO(
                "",
                "acc2",
                BigDecimal.valueOf(-10),
                date
        );

        mockMvc.perform(post("/api/v1/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.name()))
                .andExpect(jsonPath("$.message").isString());
    }
}
