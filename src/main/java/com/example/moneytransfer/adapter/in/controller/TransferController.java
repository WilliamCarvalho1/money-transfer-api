package com.example.moneytransfer.adapter.in.controller;

import com.example.moneytransfer.adapter.in.controller.dto.TransferRequestDTO;
import com.example.moneytransfer.adapter.in.controller.dto.TransferResponseDTO;
import com.example.moneytransfer.adapter.in.controller.dto.TransferUpdateRequestDTO;
import com.example.moneytransfer.adapter.in.controller.mapper.TransferUpdateWebMapper;
import com.example.moneytransfer.adapter.in.controller.mapper.TransferWebMapper;
import com.example.moneytransfer.application.port.in.CreateTransferUseCase;
import com.example.moneytransfer.application.port.in.DeleteTransferUseCase;
import com.example.moneytransfer.application.port.in.GetTransferUseCase;
import com.example.moneytransfer.application.port.in.UpdateTransferUseCase;
import com.example.moneytransfer.domain.model.Transfer;
import com.example.moneytransfer.domain.model.TransferUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfer")
public class TransferController {

    private final CreateTransferUseCase createTransferUseCase;
    private final GetTransferUseCase getTransferUseCase;
    private final UpdateTransferUseCase updateTransferUseCase;
    private final DeleteTransferUseCase deleteTransferUseCase;

    public TransferController(
            CreateTransferUseCase createTransferUseCase,
            GetTransferUseCase getTransferUseCase,
            UpdateTransferUseCase updateTransferUseCase,
            DeleteTransferUseCase deleteTransferUseCase
    ) {
        this.createTransferUseCase = createTransferUseCase;
        this.getTransferUseCase = getTransferUseCase;
        this.updateTransferUseCase = updateTransferUseCase;
        this.deleteTransferUseCase = deleteTransferUseCase;
    }

    @PostMapping
    public ResponseEntity<TransferResponseDTO> createTransfer(@Valid @RequestBody TransferRequestDTO request) {
        Transfer entity = TransferWebMapper.toDomain(request);

        return ResponseEntity.ok(TransferWebMapper.toResponseDTO(createTransferUseCase.createTransfer(entity)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponseDTO> getTransfer(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(TransferWebMapper.toResponseDTO(getTransferUseCase.getTransfer(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferResponseDTO> updateTransfer(@PathVariable @NotNull Long id,
                                                              @Valid @RequestBody TransferUpdateRequestDTO request) {
        TransferUpdate entity = TransferUpdateWebMapper.toDomain(request);

        return ResponseEntity.ok(TransferWebMapper.toResponseDTO(updateTransferUseCase.updateTransfer(id, entity)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotNull Long id) {
        deleteTransferUseCase.deleteTransfer(id);
    }
}
