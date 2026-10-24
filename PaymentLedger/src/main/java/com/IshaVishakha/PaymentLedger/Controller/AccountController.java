package com.IshaVishakha.PaymentLedger.Controller;
import com.IshaVishakha.PaymentLedger.Dto.AccountResponse;
import com.IshaVishakha.PaymentLedger.Dto.CreateAccountRequest;
import com.IshaVishakha.PaymentLedger.Dto.LedgerEntryResponse;
import com.IshaVishakha.PaymentLedger.Service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/createAccount")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID accountId){
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccount(accountId));
    }

    @GetMapping("/{accountId}/ledger")
    public ResponseEntity<Page<LedgerEntryResponse>> getLedger(@PathVariable UUID accountId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getLedger(accountId, pageable));

    }
}
