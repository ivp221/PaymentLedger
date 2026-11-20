package com.example.ledger.platform.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.aspectj.weaver.ast.Not;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("app")
@Validated
public record AppProperties(
        @Valid @NotNull Outbox outbox,
        @Valid @NotNull Idempotency idempotency,
        @Valid @NotNull Topics topics
        ){
    public record Outbox(
            @Min(1) int batchSize,
            @Min(10) long pollIntervalsMs
    ){}

    public record Idempotency(
            @Min(1) int ttlHours
    ){}

    public record Topics(
            @NotBlank String transferPosted,
            @NotBlank String transferRejected,
            @NotBlank String transferReversed
    ){}
}


