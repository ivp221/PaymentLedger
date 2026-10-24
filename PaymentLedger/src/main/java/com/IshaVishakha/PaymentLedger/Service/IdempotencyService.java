package com.IshaVishakha.PaymentLedger.Service;

import com.IshaVishakha.PaymentLedger.Dto.TransactionResponse;
import com.IshaVishakha.PaymentLedger.Entity.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface IdempotencyService {
    public Optional<TransactionResponse> getCachedResponse(String idempotencyKey);
    public void cacheResponse(String idempotencyKey, TransactionResponse response);

}
