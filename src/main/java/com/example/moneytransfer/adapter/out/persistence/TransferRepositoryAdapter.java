package com.example.moneytransfer.adapter.out.persistence;

import com.example.moneytransfer.adapter.out.persistence.entity.JpaTransferEntity;
import com.example.moneytransfer.adapter.out.persistence.mapper.TransferPersistenceMapper;
import com.example.moneytransfer.application.port.out.TransferRepositoryPort;
import com.example.moneytransfer.domain.model.Transfer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransferRepositoryAdapter implements TransferRepositoryPort {

    private final TransferJpaRepository repository;

    public TransferRepositoryAdapter(TransferJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Transfer save(Transfer transfer) {
        JpaTransferEntity entity = TransferPersistenceMapper.toJpaEntity(transfer);

        JpaTransferEntity savedEntity = repository.save(entity);

        return TransferPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return repository.findById(id)
                .map(TransferPersistenceMapper::toDomain);
    }

    @Override
    @Transactional
    public Transfer update(Transfer retrievedTransfer) {
        JpaTransferEntity savedEntity = repository.save(
                TransferPersistenceMapper.toJpaEntity(retrievedTransfer)
        );

        return TransferPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
