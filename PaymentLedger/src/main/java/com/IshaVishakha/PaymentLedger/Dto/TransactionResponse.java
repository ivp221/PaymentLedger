package com.IshaVishakha.PaymentLedger.Dto;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class TransactionResponse {
    UUID transactionId;
    UUID senderAccountId;
    UUID receiverAccountId;
    BigDecimal transactionAmount;
    String transactionCurrency;
    String transactionPaymentStatus;
    String transactionFailureReason;
    Instant createdAt;
    Instant updatedAt;

}
