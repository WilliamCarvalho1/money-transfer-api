package com.example.moneytransfer.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transfers")
public class JpaTransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "source_account", nullable = false)
    public String sourceAccount;
    @Column(name = "destination_account", nullable = false)
    public String destinationAccount;
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    public BigDecimal amount;
    @Column(name = "scheduled_date", nullable = false)
    public LocalDate scheduledDate;
    @Column(name = "fee", nullable = false, precision = 19, scale = 2)
    public BigDecimal fee;
    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    public BigDecimal totalAmount;
}
