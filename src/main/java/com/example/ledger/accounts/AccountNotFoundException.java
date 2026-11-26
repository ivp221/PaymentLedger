package com.example.ledger.accounts;

import com.example.ledger.platform.error.ApiException;
import com.example.ledger.platform.error.ErrorCode;

import java.util.UUID;

public class AccountNotFoundException extends ApiException {
    public AccountNotFoundException(UUID accountId) {
        super(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found : " + accountId);
    }
}
