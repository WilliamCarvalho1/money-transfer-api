package com.example.moneytransfer.adapter.out.persistence.mapper;

import com.example.moneytransfer.adapter.out.persistence.entity.JpaTransferEntity;
import com.example.moneytransfer.domain.model.Transfer;

public class TransferPersistenceMapper {

    private TransferPersistenceMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Transfer toDomain(JpaTransferEntity jpaTransferEntity) {
        return new Transfer(
                jpaTransferEntity.id,
                jpaTransferEntity.sourceAccount,
                jpaTransferEntity.destinationAccount,
                jpaTransferEntity.amount,
                jpaTransferEntity.scheduledDate,
                jpaTransferEntity.fee,
                jpaTransferEntity.totalAmount
        );
    }

    public static JpaTransferEntity toJpaEntity(Transfer transfer) {
        return new JpaTransferEntity(
                transfer.getId(),
                transfer.getSourceAccount(),
                transfer.getDestinationAccount(),
                transfer.getAmount(),
                transfer.getScheduledDate(),
                transfer.getFee(),
                transfer.getTotalAmount()
        );
    }


}
