package com.IshaVishakha.PaymentLedger.Service;

import com.IshaVishakha.PaymentLedger.Configuration.TransactionProperties;
import com.IshaVishakha.PaymentLedger.Dto.CreateTransactionRequest;
import com.IshaVishakha.PaymentLedger.Dto.TransactionResponse;
import com.IshaVishakha.PaymentLedger.Entity.Account;
import com.IshaVishakha.PaymentLedger.Entity.OutboxEvent;
import com.IshaVishakha.PaymentLedger.Entity.Transaction;
import com.IshaVishakha.PaymentLedger.Enums.PaymentStatus;
import com.IshaVishakha.PaymentLedger.Exception.AccountNotFoundException;
import com.IshaVishakha.PaymentLedger.Exception.CurrencyMismatchException;
import com.IshaVishakha.PaymentLedger.Exception.TransactionNotFoundException;
import com.IshaVishakha.PaymentLedger.Kafka.Events.EventType;
import com.IshaVishakha.PaymentLedger.Kafka.Events.PaymentSubmittedEvent;
import com.IshaVishakha.PaymentLedger.Mapper;
import com.IshaVishakha.PaymentLedger.Repository.AccountRepo;
import com.IshaVishakha.PaymentLedger.Repository.OutboxEventRepo;
import com.IshaVishakha.PaymentLedger.Repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepo transactionRepo;
    private final IdempotencyService idempotencyService;
    private final AccountRepo accountRepo;
    private final TransactionProperties transactionProperties;
    private final ObjectMapper objectMapper;
    private final OutboxEventRepo outboxEventRepo;


    public TransactionResponse getTransaction(UUID transactionId){
        Transaction transaction = transactionRepo.findById(transactionId).orElseThrow(()-> new TransactionNotFoundException(transactionId));
        return Mapper.toTransactionResponse(transaction);
    }

    public Page<TransactionResponse> listTransactions(Pageable pageable){
        return transactionRepo.findAllByOrderByTransactionCreatedAtDesc(pageable).map(Mapper::toTransactionResponse);
    }

    public TransactionResponse submitTransaction(CreateTransactionRequest request, String idempotencyKey){
        if (idempotencyKey!= null){
            var cached = idempotencyService.getCachedResponse(idempotencyKey);
            if (cached.isPresent()){
                log.debug("Idempotent replay for key {}" ,idempotencyKey);
                return cached.get();
            }
        }
        TransactionResponse transactionResponse;

        try {
            transactionResponse = createPaymentAndEnqueue(request, idempotencyKey);

        }catch (DataIntegrityViolationException e){
            if(idempotencyKey != null){
                Transaction existingNotCached =transactionRepo.findByTransactionIdempotencyKey(idempotencyKey).orElseThrow(()->e);
                transactionResponse = Mapper.toTransactionResponse(existingNotCached);
            }else {
                throw e;
            }
        }
        if (idempotencyKey != null){
            idempotencyService.cacheResponse(idempotencyKey, transactionResponse);
        }
        return transactionResponse;
    }

    @Transactional
    public TransactionResponse createPaymentAndEnqueue(CreateTransactionRequest request, String idempotencyKey){
        Account sender = accountRepo.findById(request.getSenderAccountId()).orElseThrow(()-> new AccountNotFoundException(request.getSenderAccountId()));
        Account receiver = accountRepo.findById(request.getReceiverAccountId()).orElseThrow(() -> new AccountNotFoundException(request.getReceiverAccountId()));

        if (!sender.getCurrency().equals(request.getCurrency()) || !receiver.getCurrency().equals(request.getCurrency())){
            throw new CurrencyMismatchException("Currency mismatch \n Sender's currency : "+ sender.getCurrency() +"\nReceiver's Currency : "+ receiver.getCurrency() + "Request Currency :" + request.getCurrency());
        }
        
        Transaction transaction = Transaction.builder().transactionId(UUID.randomUUID()).senderAccountId(sender.getId()).receiverAccountId(receiver.getId()).transactionAmount(request.getAmount()).transactionCurrency(request.getCurrency()).transactionPaymentStatus(PaymentStatus.PENDING).transactionIdempotencyKey(idempotencyKey).build();
        transactionRepo.save(transaction);

        PaymentSubmittedEvent paymentSubmittedEvent = new PaymentSubmittedEvent(transaction.getTransactionId(), transaction.getSenderAccountId(), transaction.getReceiverAccountId(), transaction.getTransactionAmount(), transaction.getTransactionCurrency(), Instant.now());

        OutboxEvent outboxEvent = OutboxEvent.builder().id(UUID.randomUUID()).entityType("Payment").entityId(transaction.getTransactionId()).eventType(EventType.PAYMENT_SUBMITTED).topic(transactionProperties.getSubmittedTopic()).partitionKey(transaction.getSenderAccountId().toString()).payload(writeJson(paymentSubmittedEvent)).build();

        outboxEventRepo.save(outboxEvent);
        return Mapper.toTransactionResponse(transaction);


    }
    private String writeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize outbox event payload", e);
        }
    }



}
