package com.IshaVishakha.PaymentLedger.Service;

import com.IshaVishakha.PaymentLedger.Kafka.Events.PaymentCompletedEvent;
import com.IshaVishakha.PaymentLedger.Kafka.Events.PaymentFailedEvent;

import java.util.UUID;

public interface NotificationService {
    public void handleCompleted(PaymentCompletedEvent event);
    public void handleFailed(PaymentFailedEvent event);




}
