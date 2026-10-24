package com.IshaVishakha.PaymentLedger.Kafka;

import com.IshaVishakha.PaymentLedger.Kafka.Events.PaymentCompletedEvent;
import com.IshaVishakha.PaymentLedger.Kafka.Events.PaymentFailedEvent;
import com.IshaVishakha.PaymentLedger.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionNotificationListener {
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(
            topics ="${app.payments.completed-topic}",
            groupId ="notification-service"
    )
    public void onTransactionCompleted(ConsumerRecord<String, String> record, Acknowledgment ack){
        try {
            PaymentCompletedEvent event = objectMapper.readValue(record.value(), PaymentCompletedEvent.class);
            notificationService.handleCompleted(event);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing payments.completed record at offset {}: {}", record.offset(), e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

    public void onTransactionFailed(ConsumerRecord<String, String> record, Acknowledgment ack){
        try {
            PaymentFailedEvent event = objectMapper.readValue(record.value(), PaymentFailedEvent.class);
            notificationService.handleFailed(event);
            ack.acknowledge();
        }catch (Exception e){
            log.error("Error processing payments.failed record at offset {}: {}", record.offset(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
