package com.IshaVishakha.PaymentLedger.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    UUID id;
    String accountHolderName;
    String currency;
    BigDecimal balance;
    Instant createdAt;
    Instant updatedAt;


}
