package com.IshaVishakha.PaymentLedger.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class LedgerEntryResponse {
    UUID id;
    UUID accountId;
    UUID paymentId;
    String operation;
    BigDecimal amount;
    BigDecimal updatedAccountBalance;
    Instant createdAt;
}
