package com.example.moneytransfer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    private Long id;
    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private LocalDate scheduledDate;
    private BigDecimal fee;
    private BigDecimal totalAmount;

}
