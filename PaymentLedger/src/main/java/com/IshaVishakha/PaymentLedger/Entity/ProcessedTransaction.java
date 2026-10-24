package com.IshaVishakha.PaymentLedger.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;


// a processed transaction has already been applied to the ledger. Inserting this row and updating both wallets must happen in the same transaction. Ensures Idempotency in ledger updates. If transactionId is already in the table then the required ledger operations have already been completed and do not have to be done again.

@Entity
@Table(name = "processed_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name ="processed_transaction_id")
    private UUID processedTransactionId;

    @CreationTimestamp
    @Column(name = "processed_at", nullable = false, updatable = false)
    private Instant processedAt;

    public ProcessedTransaction(UUID transactionId){
        processedTransactionId = transactionId;
    }


}
