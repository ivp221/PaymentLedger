package com.IshaVishakha.PaymentLedger.Kafka.Events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentSubmittedEvent (UUID transactionId, UUID senderAccountId, UUID receiverAccountId, BigDecimal amount , String currency, Instant submittedAt){}
