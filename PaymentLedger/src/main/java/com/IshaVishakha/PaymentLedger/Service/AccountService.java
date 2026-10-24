package com.IshaVishakha.PaymentLedger.Service;

import com.IshaVishakha.PaymentLedger.Dto.AccountResponse;
import com.IshaVishakha.PaymentLedger.Dto.CreateAccountRequest;
import com.IshaVishakha.PaymentLedger.Dto.LedgerEntryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    public AccountResponse createAccount(CreateAccountRequest request);
    public AccountResponse getAccount(UUID accountId);
    public Page<LedgerEntryResponse> getLedger(UUID accountId, Pageable pageable);
}
