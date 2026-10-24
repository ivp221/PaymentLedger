package com.IshaVishakha.PaymentLedger.Configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.outbox")
public class OutboxProperties {
    private int batchSize = 20;
    private long pollIntervalMs = 100;
}
