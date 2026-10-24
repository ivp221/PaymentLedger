package com.IshaVishakha.PaymentLedger.Service;


import com.IshaVishakha.PaymentLedger.Dto.AccountResponse;
import com.IshaVishakha.PaymentLedger.Dto.CreateAccountRequest;
import com.IshaVishakha.PaymentLedger.Dto.LedgerEntryResponse;
import com.IshaVishakha.PaymentLedger.Entity.Account;
import com.IshaVishakha.PaymentLedger.Entity.LedgerEntry;
import com.IshaVishakha.PaymentLedger.Exception.AccountNotFoundException;
import com.IshaVishakha.PaymentLedger.Mapper;
import com.IshaVishakha.PaymentLedger.Repository.AccountRepo;
import com.IshaVishakha.PaymentLedger.Repository.LedgerEntryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.IshaVishakha.PaymentLedger.Mapper.toAccountResponse;
import static com.IshaVishakha.PaymentLedger.Mapper.toLedgerEntryResponse;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService{
    private static final String BALANCE_CACHE_PREFIX = "balance:account:";
    private final AccountRepo accountRepo;
    private final LedgerEntryRepo ledgerEntryRepo;



    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        Account account = Account.builder().id(UUID.randomUUID()).accountHolderName(request.getAccountHolderName()).currency(request.getCurrency()).accountBalance(BigDecimal.ZERO).build();
        accountRepo.save(account);
        return toAccountResponse(account);
    }

    @Override
    public AccountResponse getAccount(UUID accountId) {
        Account account = accountRepo.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        return new AccountResponse(account.getId(), account.getAccountHolderName(), account.getCurrency(), account.getAccountBalance(), account.getCreatedAt(), account.getUpdatedAt());

    }


    @Override
    public Page<LedgerEntryResponse> getLedger(UUID accountId, Pageable pageable) {
        return ledgerEntryRepo.findByAccountIdOrderByCreatedAtDesc(accountId, pageable).map(Mapper::toLedgerEntryResponse);
    }

}
