package com.IshaVishakha.PaymentLedger.Entity;

import com.IshaVishakha.PaymentLedger.Enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    @Column(name ="sender_account_id", nullable = false)
    private UUID senderAccountId;

    @Column(name = " receiver_account_id", nullable = false)
    private UUID receiverAccountId;

    @Column(name = "transaction_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal transactionAmount;

    @Column(name = "transactionCurrency", nullable = false, length = 3)
    private String transactionCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name ="transaction_payment_status", nullable = false)
    private PaymentStatus transactionPaymentStatus;

    @Column(name = "transaction_idempotency_key")
    private String transactionIdempotencyKey;

    @Column(name ="transaction_failure_reason")
    private String transactionFailureReason;

    @CreationTimestamp
    @Column(name = "transaction_created_at", nullable = false, updatable = false)
    private Instant transactionCreatedAt;

    @UpdateTimestamp
    @Column(name ="transaction_updated_at", nullable = false)
    private Instant updatedAt;


}
