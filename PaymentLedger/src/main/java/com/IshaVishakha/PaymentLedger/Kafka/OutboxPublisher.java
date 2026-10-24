package com.IshaVishakha.PaymentLedger.Kafka;

import com.IshaVishakha.PaymentLedger.Configuration.OutboxProperties;
import com.IshaVishakha.PaymentLedger.Entity.OutboxEvent;
import com.IshaVishakha.PaymentLedger.Repository.OutboxEventRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
@Slf4j
//looks at theoutbox_events table and unpublished rows are sent to kafka
public class OutboxPublisher {

    private final OutboxEventRepo outboxEventRepo;
    private final OutboxProperties outboxProperties;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Scheduled(fixedDelayString = "${app.outbox.poll-interval-ms:100}")
    @Transactional
    public void publishPendingEvents(){
        List<OutboxEvent> batch = outboxEventRepo.findUnpublishedBatchForUpdate(outboxProperties.getBatchSize());
        if (batch.isEmpty()) return;
        for (OutboxEvent event : batch){
            try {
                kafkaTemplate.send(event.getTopic(), event.getPartitionKey(), event.getPayload()).get(5, TimeUnit.SECONDS);
                event.setPublishedAt(Instant.now());
                outboxEventRepo.save(event);
            } catch (Exception e) {
                log.error("Failed to publish outbox event {} to topic {}" ,event.getId() , event.getTopic());
            }
        }

    }

}
