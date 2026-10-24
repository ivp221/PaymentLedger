package com.IshaVishakha.PaymentLedger.Configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.payments")
public class TransactionProperties {
    private int IdempotencyTtlHours = 24;
    private String submittedTopic = "payments.submitted";
    private String completedTopic = "payments.completed";
    private String failedTopic = "payments.failed";}
