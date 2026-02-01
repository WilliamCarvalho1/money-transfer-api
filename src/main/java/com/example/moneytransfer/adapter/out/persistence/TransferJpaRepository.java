package com.example.moneytransfer.adapter.out.persistence;

import com.example.moneytransfer.adapter.out.persistence.entity.JpaTransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferJpaRepository extends JpaRepository<JpaTransferEntity, Long> {
}
