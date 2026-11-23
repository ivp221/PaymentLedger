package com.example.ledger.accounts;

import com.example.ledger.platform.error.ApiException;
import com.example.ledger.platform.error.ErrorCode;

import java.util.Currency;
import java.util.UUID;

public record NewAccount (
        UUID ownerId,
        String holderName,
        AccountType type,
        String currency,
        boolean allowNegative
){

    public NewAccount{
        if (holderName == null || holderName.isEmpty()){
            throw new ApiException(ErrorCode.VALIDATION_FAILED, "holderName is required");
        }
        if (type == null){
            throw new ApiException(ErrorCode.VALIDATION_FAILED, "type is required");
        }
        if (currency == null){
            throw new ApiException(ErrorCode.VALIDATION_FAILED, "currency is required");
        }
        try {
            Currency c = Currency.getInstance(currency);
            if (c.getDefaultFractionDigits() < 0){
                throw new IllegalArgumentException();
            }
        }catch(IllegalArgumentException ex){
            throw new ApiException(ErrorCode.VALIDATION_FAILED, "Unknown ISO 4217 currency " + currency);
        }
        if (ownerId != null && allowNegative){
            throw new ApiException(ErrorCode.VALIDATION_FAILED, "Customer accounts cannot have allowNegative as true");
        }
    }

    public static NewAccount wallet(UUID ownerId, String holderName, String currency){
        return new NewAccount(ownerId, holderName, AccountType.LIABILITY,currency, false);
    }

    public static NewAccount system(String name, AccountType type, String currency, boolean allowNegative){
        return new NewAccount(null, "", type ,currency, allowNegative);
    }
}
