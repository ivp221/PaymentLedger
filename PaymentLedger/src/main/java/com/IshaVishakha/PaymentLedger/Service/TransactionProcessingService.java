package com.IshaVishakha.PaymentLedger.Service;

import com.IshaVishakha.PaymentLedger.Entity.Transaction;
import com.IshaVishakha.PaymentLedger.Kafka.Events.PaymentSubmittedEvent;

public interface TransactionProcessingService {
    public void process(PaymentSubmittedEvent event);



}
