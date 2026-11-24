package com.example.ledger.accounts;

import java.time.Instant;
import java.util.UUID;

public record Account (
        UUID id,
        UUID ownerId,
        String holderName,
        AccountType type,
        String currency,
        AccountStatus status,
        boolean allowNegative,
        long postedBalance,
        long heldAmount,
        long version,
        Instant createdAt,
        Instant updatedAt
){
    public long availableBalance(){
        return postedBalance-heldAmount;
    }
}
