package com.IshaVishakha.PaymentLedger.Service;

import com.IshaVishakha.PaymentLedger.Configuration.TransactionProperties;
import com.IshaVishakha.PaymentLedger.Entity.*;
import com.IshaVishakha.PaymentLedger.Enums.Operation;
import com.IshaVishakha.PaymentLedger.Enums.PaymentStatus;
import com.IshaVishakha.PaymentLedger.Exception.AccountNotFoundException;
import com.IshaVishakha.PaymentLedger.Kafka.Events.EventType;
import com.IshaVishakha.PaymentLedger.Kafka.Events.PaymentCompletedEvent;
import com.IshaVishakha.PaymentLedger.Kafka.Events.PaymentFailedEvent;
import com.IshaVishakha.PaymentLedger.Kafka.Events.PaymentSubmittedEvent;
import com.IshaVishakha.PaymentLedger.Repository.*;
import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionProcessingServiceImpl {
    private final ProcessedTransactionRepo processedTransactionRepo;
    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;
    private final LedgerEntryRepo ledgerEntryRepo;
    private final TransactionProperties transactionProperties;
    private final ObjectMapper objectMapper;
    private final OutboxEventRepo outboxEventRepo;


    @Transactional
    public void process(PaymentSubmittedEvent event){
        if(processedTransactionRepo.existsById(event.transactionId())){
            log.info("Transaction already processed with transaction id {}", event.transactionId());
            return;
        }
        Transaction transaction = TransactionRepo.findByTransactionId(event.transactionId()).orElseThrow(() -> new IllegalStateException("unknown transaction corresponding to id"+ event.transactionId()));

        if (transaction.getTransactionPaymentStatus() != PaymentStatus.PENDING){
            log.info("Payment {} is already in terminal state {}, skipping", transaction.getTransactionId(), transaction.getTransactionPaymentStatus());
            return;
        }
        UUID firstLockId = event.senderAccountId().compareTo(event.receiverAccountId()) <= 0 ? event.senderAccountId() : event.receiverAccountId();
        UUID secondLockId = firstLockId.equals(event.receiverAccountId()) ? event.senderAccountId() : event.receiverAccountId();

        Account first = accountRepo.findByIdForUpdate(firstLockId).orElseThrow(()-> new AccountNotFoundException(firstLockId));
        Account second = accountRepo.findByIdForUpdate(secondLockId).orElseThrow(() -> new AccountNotFoundException(secondLockId));

        Account sender = first.getId().equals(event.senderAccountId())? first : second;
        Account receiver = first.getId().equals(event.senderAccountId()) ? second : first;

        if (receiver.getAccountBalance().compareTo(event.amount()) <0){
            failTransaction(transaction, "INSUFFICIENT_FUNDS");
            return;
        }
        applyTransaction(transaction, sender, receiver);
    }

    private void applyTransaction(Transaction transaction, Account sender, Account receiver){
        sender.debit(transaction.getTransactionAmount());
        receiver.credit(transaction.getTransactionAmount());
        accountRepo.save(sender);
        accountRepo.save(receiver);
        Instant now = Instant.now();
        ledgerEntryRepo.save(LedgerEntry.builder().id(UUID.randomUUID()).accountId(transaction.getSenderAccountId()).paymentId(transaction.getTransactionId()).operation(Operation.DEBIT).amount(transaction.getTransactionAmount()).updatedAccountBalance(sender.getAccountBalance()).build());
        ledgerEntryRepo.save(LedgerEntry.builder().id(UUID.randomUUID()).accountId(transaction.getReceiverAccountId()).paymentId(transaction.getTransactionId()).operation(Operation.CREDIT).amount(transaction.getTransactionAmount()).updatedAccountBalance(receiver.getAccountBalance()).build());

        transaction.setTransactionPaymentStatus(PaymentStatus.SUCCESS);
        transactionRepo.save(transaction);

        processedTransactionRepo.save(ProcessedTransaction.builder().processedTransactionId(transaction.getTransactionId()).build());

        PaymentCompletedEvent paymentCompletedEvent = new PaymentCompletedEvent(transaction.getTransactionId(), transaction.getSenderAccountId(), transaction.getReceiverAccountId(), transaction.getTransactionAmount(), transaction.getTransactionCurrency(), now);

        enqueueOutbox(transaction.getTransactionId(), EventType.PAYMENT_COMPLETED, transactionProperties.getCompletedTopic(), sender.getId().toString(), paymentCompletedEvent);





    }
    private void failTransaction(Transaction transaction, String reason){
        transaction.setTransactionPaymentStatus(PaymentStatus.FAILED);
        transaction.setTransactionFailureReason(reason);
        transactionRepo.save(transaction);

        processedTransactionRepo.save(ProcessedTransaction.builder().processedTransactionId(transaction.getTransactionId()).build());

        PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(transaction.getTransactionId(), transaction.getSenderAccountId(), transaction.getReceiverAccountId(), transaction.getTransactionAmount(), transaction.getTransactionCurrency(), reason, Instant.now());

        enqueueOutbox(transaction.getTransactionId(), EventType.PAYMENT_FAILED, transactionProperties.getFailedTopic(), transaction.getSenderAccountId().toString(), paymentFailedEvent);
        log.warn("Payment {} failed: {}", transaction.getTransactionId(), reason);
    }

    private void enqueueOutbox(UUID transactionId, String eventType, String topic, String partitionKey, Object payload){
        outboxEventRepo.save(OutboxEvent.builder().id(UUID.randomUUID()).entityType("Transaction").entityId(transactionId).eventType(eventType).topic(topic).partitionKey(partitionKey).payload(writeJson(payload)).build());
    }

    private String writeJson(Object value){
        try{
            return objectMapper.writeValueAsString(value);
        }catch(Exception e){
            throw new IllegalStateException("Failed to serialize outbox event payload", e);
        }
    }
}
