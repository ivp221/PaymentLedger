package com.IshaVishakha.PaymentLedger.Kafka.Events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

//later published to payments.completed
public record PaymentCompletedEvent(UUID transactionId, UUID senderAccountId, UUID receiverAccountId, BigDecimal amount , String currency, Instant completedAt) {}
