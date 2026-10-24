package com.IshaVishakha.PaymentLedger.Exception;

import java.util.UUID;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(UUID transactionId) {
        super("Could not find Transaction with TransactionId : "+ transactionId);
    }
}
