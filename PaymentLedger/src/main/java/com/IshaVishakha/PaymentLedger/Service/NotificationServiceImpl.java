package com.IshaVishakha.PaymentLedger.Service;

import com.IshaVishakha.PaymentLedger.Entity.Notification;
import com.IshaVishakha.PaymentLedger.Kafka.Events.EventType;
import com.IshaVishakha.PaymentLedger.Kafka.Events.PaymentCompletedEvent;
import com.IshaVishakha.PaymentLedger.Kafka.Events.PaymentFailedEvent;
import com.IshaVishakha.PaymentLedger.Repository.NotificationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements  NotificationService{
    private final NotificationRepo notificationRepo;

    private void record(UUID transactionId, String eventType, String message){
        notificationRepo.save(Notification.builder().id(UUID.randomUUID()).transactionId(transactionId).eventType(eventType).message(message).build());

    }

    @Override
    public void handleCompleted(PaymentCompletedEvent event) {
        record(event.transactionId(), EventType.PAYMENT_COMPLETED, "Payment of "+ event.amount() +" "+ event.currency() +"completed successfully");

    }

    @Override
    public void handleFailed(PaymentFailedEvent event) {
        record(event.transactionId(), EventType.PAYMENT_FAILED, "Payment of "+ event.amount() +" "+ event.currency() +"failed.\nReason :" + event.reason());
    }
}
