package com.IshaVishakha.PaymentLedger.Repository;

import com.IshaVishakha.PaymentLedger.Entity.ProcessedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessedTransactionRepo extends JpaRepository<ProcessedTransaction, UUID> {
}
