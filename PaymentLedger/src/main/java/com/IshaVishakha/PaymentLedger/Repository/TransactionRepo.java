package com.IshaVishakha.PaymentLedger.Repository;

import com.IshaVishakha.PaymentLedger.Entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepo extends JpaRepository<Transaction, UUID> {

    static Optional<Transaction> findByTransactionId(UUID transactionId) {
        return null;
    }

    Optional<Transaction> findByTransactionIdempotencyKey(String idempotencyKey);
    Page<Transaction>  findAllByOrderByTransactionCreatedAtDesc(Pageable pageable);
}
