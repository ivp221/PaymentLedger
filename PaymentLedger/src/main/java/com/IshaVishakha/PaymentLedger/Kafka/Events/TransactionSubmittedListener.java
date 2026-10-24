package com.IshaVishakha.PaymentLedger.Kafka.Events;

import com.IshaVishakha.PaymentLedger.Service.TransactionProcessingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionSubmittedListener {
    private final ObjectMapper objectMapper;
    private final TransactionProcessingServiceImpl transactionProcessingService;


    @KafkaListener(
            topics = "${app.payments.submitted-topic}",
            groupId = "ledger-processor",
            concurrency = "3")
    public void onTransactionSubmitted(ConsumerRecord<String, String> record, Acknowledgment ack){
        try {
            PaymentSubmittedEvent event = objectMapper.readValue(record.value(), PaymentSubmittedEvent.class);
            transactionProcessingService.process(event);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing payments.submitted record at offset {}: {}", record.offset(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
