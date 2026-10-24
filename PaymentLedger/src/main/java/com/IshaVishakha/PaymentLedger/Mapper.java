package com.IshaVishakha.PaymentLedger;

import com.IshaVishakha.PaymentLedger.Dto.AccountResponse;
import com.IshaVishakha.PaymentLedger.Dto.LedgerEntryResponse;
import com.IshaVishakha.PaymentLedger.Dto.TransactionResponse;
import com.IshaVishakha.PaymentLedger.Entity.Account;
import com.IshaVishakha.PaymentLedger.Entity.LedgerEntry;
import com.IshaVishakha.PaymentLedger.Entity.Transaction;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Mapper {
    public static AccountResponse toAccountResponse(Account account){
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountHolderName(account.getAccountHolderName());
        accountResponse.setBalance(account.getAccountBalance());
        accountResponse.setCreatedAt(account.getCreatedAt());
        accountResponse.setCurrency(account.getCurrency());
        accountResponse.setId(account.getId());
        accountResponse.setUpdatedAt(account.getUpdatedAt());
        return accountResponse;
    }

    public static LedgerEntryResponse toLedgerEntryResponse(LedgerEntry ledgerEntry){
        return new LedgerEntryResponse(ledgerEntry.getId(), ledgerEntry.getAccountId(), ledgerEntry.getPaymentId(), ledgerEntry.getOperation().toString(), ledgerEntry.getAmount(), ledgerEntry.getUpdatedAccountBalance(), ledgerEntry.getCreatedAt());

    }

    public static TransactionResponse toTransactionResponse(Transaction transaction){
        return new TransactionResponse(transaction.getTransactionId(), transaction.getSenderAccountId(), transaction.getReceiverAccountId(), transaction.getTransactionAmount(), transaction.getTransactionCurrency(), transaction.getTransactionPaymentStatus().toString(), transaction.getTransactionFailureReason(), transaction.getTransactionCreatedAt(), transaction.getUpdatedAt());
    }
}
