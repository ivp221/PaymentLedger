package com.IshaVishakha.PaymentLedger.Entity;

import com.IshaVishakha.PaymentLedger.Enums.Operation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name ="ledger_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name ="account_id", nullable = false)
    private UUID accountId;

    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;

    @Column(name ="operation", nullable = false)
    private Operation operation;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "updated_account_balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal updatedAccountBalance;

    @CreationTimestamp
    @Column(name ="created_at", nullable = false, updatable = false)
    private Instant createdAt;

}
