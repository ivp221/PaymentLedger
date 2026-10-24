package com.IshaVishakha.PaymentLedger.Service;

import com.IshaVishakha.PaymentLedger.Dto.CreateTransactionRequest;
import com.IshaVishakha.PaymentLedger.Dto.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransactionService {
    public TransactionResponse getTransaction(UUID transactionId);
    public Page<TransactionResponse> listTransactions(Pageable pageable);
    public TransactionResponse submitTransaction(CreateTransactionRequest request, String idempotencyKey);

}
